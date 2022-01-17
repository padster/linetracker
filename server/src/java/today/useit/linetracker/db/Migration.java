package today.useit.linetracker.db;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;mvn clean compile list all files
import com.sun.net.httpserver.HttpServer;

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

    System.out.println("TODO: Write migration code...");
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
