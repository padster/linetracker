/****

Still remaining:
 * UI: Graph doesn't show values on click.
 * Run webapp on GCP
 * Add support for google authentication
 * Map auth ID to old ID
 * Clean up BaseLoader (& other loader comments) & Migration class.
 * run!

*****/

package today.useit.linetracker.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.gson.Gson;

import jakarta.inject.Provider;
import javafx.util.Pair;
import today.useit.linetracker.db.transforms.ComposLineLoader;
import today.useit.linetracker.db.transforms.DatedValueLoader;
import today.useit.linetracker.db.transforms.GraphsLineLoader;
import today.useit.linetracker.db.transforms.LineTypeLoader;
import today.useit.linetracker.db.transforms.SettingsLoader;
import today.useit.linetracker.db.transforms.SingleLineLoader;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.model.DatedValue;
import today.useit.linetracker.model.GraphsLineMeta;
import today.useit.linetracker.model.HasChildren;
import today.useit.linetracker.model.HasId;
import today.useit.linetracker.model.Settings;
import today.useit.linetracker.model.SingleLineMeta;
import today.useit.linetracker.store.ItemStore;
import today.useit.linetracker.store.Stores;
import today.useit.linetracker.store.ValuesStore;
import today.useit.linetracker.store.cloud.CloudStores;


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
      // Set<String> childIds = line.children().stream().map(c -> c.id).collect(Collectors.toSet());
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
        .map(child -> child.id != null
          ? new ChildEntry(child.type, idRemap.get(child.id))
          : new ChildEntry(child.type, child.value)
        )
        .collect(Collectors.toList())
    );
  }

  public static <T extends HasId> void writeLines(
    List<T> lines, ItemStore<T> store, Map<String, String> idRemap
  ) {
    int linesWritten = 0;
    for (T line : lines) {
      String oldId = line.id();
      if (line instanceof HasChildren) {
        Migration.remapIDs((HasChildren) line, idRemap);
      }
      String newId = store.createItem(line).id();
      idRemap.put(oldId, newId);
      linesWritten++;
    }
    logger.info("Wrote " + linesWritten + " lines");
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

      Map<String, String> idRemap = new HashMap<>();
      if (stores != null) {
        writeLines(sData, stores.singleStore(), idRemap);
        cData = Migration.topsort(cData);
        writeLines(cData, stores.composStore(), idRemap);
        writeLines(gData, stores.graphsStore(), idRemap);
      }

      logger.info("Loading values...");
      List<Pair<String, DatedValue>> values = new DatedValueLoader(gson, uidToMigrate).loadAll();
      logger.info(values.size() + " values lines loaded!");
      if (stores != null) {
        writeValues(values, stores.valuesStore(), idRemap);
      }

      logger.info("Loading settings...");
      List<Settings> setData = new SettingsLoader(gson, uidToMigrate).loadAll();
      if (setData.size() != 1) {
        logger.severe("Wrong settings info: Expected 1 entry, received " + setData.size());
        throw new IllegalStateException();
      }
      Settings userSetting = setData.get(0);
      if (userSetting.homeID != null) {
        userSetting = new Settings(idRemap.get(userSetting.homeID));
      }
      if (stores != null && userSetting.homeID != null) {
        stores.settingsStore().updateSettings(userSetting);
      }


    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private static void migrateUser(String userId, String email) {
    // Migrate to datastore
    Datastore db = DatastoreOptions.newBuilder()
      .setProjectId("useful-theory-217216")
      .setDatabaseId("linetracker")
      .build().getService();
    Provider<String> userProvider = new Provider<String>() {
      public String get() {
        return email;
      }
    };
    Stores stores = new CloudStores(db, userProvider);
    loadData(userId, stores); // one user at a time.
  }

  //
  // USER deletion
  //

  private static void removeData(Stores stores) {
    ValuesStore valuesStore = stores.valuesStore();

    ItemStore<GraphsLineMeta> graphStore = stores.graphsStore();
    List<GraphsLineMeta> graphs = graphStore.listItems();
    System.out.println("Removing " + graphs.size() + " graphs...");
    for (GraphsLineMeta graph : graphs) {
      graphStore.deleteItem(graph.id());
    }
    System.out.println("Graphs removed.");

    ItemStore<ComposLineMeta> composStore = stores.composStore();
    List<ComposLineMeta> compos = composStore.listItems();
    System.out.println("Removing " + compos.size() + " compos...");
    for (ComposLineMeta composLine : compos) {
      composStore.deleteItem(composLine.id());
    }
    System.out.println("Compos removed.");

    ItemStore<SingleLineMeta> singleStore = stores.singleStore();
    List<SingleLineMeta> singles = singleStore.listItems();
    System.out.println("Removing " + singles.size() + " singles...");
    for (SingleLineMeta single : singles) {
      valuesStore.removeAllValuesFromSingleLine(single.id);
      singleStore.deleteItem(single.id());
    }
    System.out.println("Singles removed.");

    stores.settingsStore().updateSettings(new Settings("DELETEME"));   
  }

  private static void deleteUser(String email) {
    Datastore db = DatastoreOptions.newBuilder()
      .setProjectId("useful-theory-217216")
      .setDatabaseId("linetracker")
      .build().getService();
    Provider<String> userProvider = new Provider<String>() {
      public String get() {
        return email;
      }
    };
    Stores stores = new CloudStores(db, userProvider);
    removeData(stores);
  }

  public static void main(String[] args) {
    // Set up logging to file...
    try {
      logger.addHandler(new FileHandler("debug.log", true));
    } catch (java.io.IOException e) {
      System.err.println("Can't log, quitting.");
      return;
    }

    deleteUser("padsterpat@gmail.com");
    migrateUser("113641087749801482038",  "padsterpat@gmail.com");
  }

  // If a --flagName is given, return the next string, otherwise null.
  // private static String maybeGetFlag(String[] args, String flagName) {
  //   String toMatch = "--" + flagName;
  //   for (int i = 0; i + 1 < args.length; i++) {
  //     if (toMatch.equals(args[i])) {
  //       return args[i + 1];
  //     }
  //   }
  //   return null;
  // }
}
