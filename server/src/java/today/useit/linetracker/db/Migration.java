/****

Still remaining:
 * Thread through uid to each section
 * Add settings loader
 * Memory check loading everything at once
 * Switch to write mode against in-memory DB
 * Clean up BaseLoader (& other loader comments)
 * run!

*****/

package today.useit.linetracker.db;

import today.useit.linetracker.db.transforms.*;
import today.useit.linetracker.model.*;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.sun.net.httpserver.HttpServer;

import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


/**
 * Migration server entrypoint.
 */
public class Migration {
  private static final Logger logger = Logger.getLogger("DEBUG");

  public static void main(String[] args) {
    // Set up logging to file...
    try {
      logger.addHandler(new FileHandler("debug.log", true));
    } catch (java.io.IOException e) {
      System.err.println("Can't log, quitting.");
      return;
    }


    Gson gson = new Gson();
    LineTypeLoader lineTypes = new LineTypeLoader(gson).preload();

    try {
      // logger.info("Loading single lines...");
      // List<SingleLineMeta> sData = new SingleLineLoader(gson).loadAll();
      // logger.info(sData.size() + " single lines loaded!");

      // logger.info("Loading compos lines...");
      // List<ComposLineMeta> cData = new ComposLineLoader(gson, lineTypes).loadAll();
      // logger.info(cData.size() + " compos lines loaded!");

      // logger.info("Loading graphs lines...");
      // List<GraphsLineMeta> gData = new GraphsLineLoader(gson, lineTypes).loadAll();
      // logger.info(gData.size() + " graphs lines loaded!");

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
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
