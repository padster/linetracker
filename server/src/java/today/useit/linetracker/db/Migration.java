/****

Still remaining:
 * Write all remaining items into memory store.
 * Clean up BaseLoader (& other loader comments)
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


/**
 * Migration server entrypoint.
 */
public class Migration {
  private static final Logger logger = Logger.getLogger("DEBUG");

  public static void writeSingle(
    List<SingleLineMeta> singleLines,
    ItemStore<SingleLineMeta> store,
    Map<String, String> idRemap
  ) {
    for (SingleLineMeta line : singleLines) {
      String oldId = line.id();
      String newId = store.createItem(line).id();
      idRemap.put(oldId, newId);
      System.out.println(oldId + " -> " + newId);
    }
  }

  public static void loadData(String uidToMigrate, Stores stores) {
    Gson gson = new Gson();
    LineTypeLoader lineTypes = new LineTypeLoader(gson, uidToMigrate).preload();

    try {
      logger.info("Loading single lines...");
      List<SingleLineMeta> sData = new SingleLineLoader(gson, uidToMigrate).loadAll();
      logger.info(sData.size() + " single lines loaded!");

      // logger.info("Loading compos lines...");
      // List<ComposLineMeta> cData = new ComposLineLoader(gson, uidToMigrate, lineTypes).loadAll();
      // logger.info(cData.size() + " compos lines loaded!");

      // logger.info("Loading graphs lines...");
      // List<GraphsLineMeta> gData = new GraphsLineLoader(gson, uidToMigrate, lineTypes).loadAll();
      // logger.info(gData.size() + " graphs lines loaded!");

      // logger.info("Loading settings...");
      // List<Settings> setData = new SettingsLoader(gson, uidToMigrate).loadAll();
      // if (setData.size() != 1) {
      //   logger.severe("Wrong settings info: Expected 1 entry, received " + setData.size());
      //   throw new IllegalStateException();
      // }
      // Settings userSetting = setData.get(0);

      // TODO - load individual values for the sData lines

      Map<String, String> idRemap = new HashMap<>();
      if (stores != null) {
        writeSingle(sData, stores.singleStore(), idRemap);
      }

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
