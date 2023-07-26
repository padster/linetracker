/****

Still remaining:
 * Write settings into store.
 * Fix performance of composite line calculator.
 * Clean up BaseLoader (& other loader comments) & Migration class.
 * UI: Fix sorting of lines in list.
 * run!

*****/

package today.useit.linetracker.db;

import today.useit.linetracker.db.transforms.*;
import today.useit.linetracker.model.*;
import today.useit.linetracker.store.*;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.sun.net.httpserver.HttpServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.util.Pair;


/**
 * Migration server entrypoint.
 */
public class Migration {
  private static final Logger logger = Logger.getLogger("DEBUG");

  // TODO - optimize? Can be faster.
  public static <T extends HasChildren & HasId> List<T> topsort(List<T> initial) {
    if (initial.isEmpty()) {
      return initial;
    }

    Set<String> toSortIDs = initial.stream().map(l -> l.id()).collect(Collectors.toSet());

    List<T> processed = new ArrayList<>();
    List<T> remaining = new ArrayList<>();

    for (T line : initial) {
      Set<String> childIds = line.children().stream().map(c -> c.id).collect(Collectors.toSet());
      boolean hasChildToSort = line.children().stream()
        .anyMatch(child -> toSortIDs.contains(child.id));
      if (hasChildToSort) {
        remaining.add(line);
      } else {
        processed.add(line);
      }
    }

    if (initial.size() == remaining.size()) {
      logger.warning("Uhoh - can't process " + initial.get(0).id());
      throw new IllegalStateException();
    }

    processed.addAll(topsort(remaining));
    return processed;
  }

  public static void remapIDs(HasChildren meta, Map<String, String> idRemap) {
    meta.setChildren(
      meta.children().stream()
        .map(child -> new ChildEntry(child.type, idRemap.get(child.id)))
        .collect(Collectors.toList())
    );
  }

  public static void writeSingle(
    List<SingleLineMeta> singleLines,
    ItemStore<SingleLineMeta> store,
    Map<String, String> idRemap
  ) {
    for (SingleLineMeta line : singleLines) {
      String oldId = line.id();
      String newId = store.createItem(line).id();
      idRemap.put(oldId, newId);
    }
  }

  public static void writeCompos(
    List<ComposLineMeta> composLines,
    ItemStore<ComposLineMeta> store,
    Map<String, String> idRemap
  ) {
    for (ComposLineMeta line : composLines) {
      String oldId = line.id();
      Migration.remapIDs(line, idRemap);
      String newId = store.createItem(line).id();
      idRemap.put(oldId, newId);
    }
  }

  public static void writeGraphs(
    List<GraphsLineMeta> composLines,
    ItemStore<GraphsLineMeta> store,
    Map<String, String> idRemap
  ) {
    for (GraphsLineMeta line : composLines) {
      String oldId = line.id();
      Migration.remapIDs(line, idRemap);
      String newId = store.createItem(line).id();
      idRemap.put(oldId, newId);
    }
  }

  public static void writeValues(
    List<Pair<String, DatedValue>> values,
    ValuesStore store,
    Map<String, String> idRemap
  ) {
    Map<String, List<DatedValue>> valuesByLine = new HashMap<>();
    for (Pair<String, DatedValue> value : values) {
      String newId = idRemap.get(value.getKey());
      if (newId != null) {
        if (!valuesByLine.containsKey(newId)) {
          valuesByLine.put(newId, new ArrayList<>());
        }
        valuesByLine.get(newId).add(value.getValue());
      }
    }

    valuesByLine.forEach( (k, v) -> {
      logger.info("Writing " + v.size() + " values to " + k);
      store.addValuesToSingleLine(k, v);
    });
  }


  public static void loadData(String uidToMigrate, Stores stores) {
    Gson gson = new Gson();
    LineTypeLoader lineTypes = new LineTypeLoader(gson, uidToMigrate).preload();

    try {
      logger.info("Loading single lines...");
      List<SingleLineMeta> sData = new SingleLineLoader(gson, uidToMigrate).loadAll();
      logger.info(sData.size() + " single lines loaded!");

      logger.info("Loading compos lines...");
      List<ComposLineMeta> cData = new ComposLineLoader(gson, uidToMigrate, lineTypes).loadAll();
      logger.info(cData.size() + " compos lines loaded!");

      logger.info("Loading graphs lines...");
      List<GraphsLineMeta> gData = new GraphsLineLoader(gson, uidToMigrate, lineTypes).loadAll();
      logger.info(gData.size() + " graphs lines loaded!");

      // logger.info("Loading settings...");
      // List<Settings> setData = new SettingsLoader(gson, uidToMigrate).loadAll();
      // if (setData.size() != 1) {
      //   logger.severe("Wrong settings info: Expected 1 entry, received " + setData.size());
      //   throw new IllegalStateException();
      // }
      // Settings userSetting = setData.get(0);

      Map<String, String> idRemap = new HashMap<>();
      if (stores != null) {
        writeSingle(sData, stores.singleStore(), idRemap);
        cData = Migration.topsort(cData);
        writeCompos(cData, stores.composStore(), idRemap);
        writeGraphs(gData, stores.graphsStore(), idRemap);
      }

      logger.info("Loading values...");
      List<Pair<String, DatedValue>> values = new DatedValueLoader(gson, uidToMigrate).loadAll();
      logger.info(values.size() + " values lines loaded!");
      writeValues(values, stores.valuesStore(), idRemap);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public static void main(String[] args) {
    // Set up logging to file...
    try {
      logger.addHandler(new FileHandler("debug.log", true));
    } catch (java.io.IOException e) {
      System.err.println("Can't log, quitting.");
      return;
    }

    loadData("113641087749801482038", null); // one user at a time.
  }

  // If a --flagName is given, return the next string, otherwise null.
  private static String maybeGetFlag(String[] args, String flagName) {
    String toMatch = "--" + flagName;
    for (int i = 0; i + 1 < args.length; i++) {
      if (toMatch.equals(args[i])) {
        return args[i + 1];
      }
    }
    return null;
  }
}
