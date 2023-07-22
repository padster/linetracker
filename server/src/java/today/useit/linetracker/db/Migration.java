package today.useit.linetracker.db;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.sun.net.httpserver.HttpServer;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

// TODO - remove
import today.useit.linetracker.db.transforms.ComposLineLoader;

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
    try {
      ComposLineLoader t = new ComposLineLoader(gson);
      System.out.println(t.loadAll());
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
