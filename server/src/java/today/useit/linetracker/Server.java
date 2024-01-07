package today.useit.linetracker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.sun.net.httpserver.HttpServer;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

// HACK - in-memory migrate first
import today.useit.linetracker.db.Migration;
import today.useit.linetracker.store.Stores;

/**
 * The actual server entrypoint - parses flags, and runs an HttpServer obtained from Guice.
 */
public class Server {
  private static final Logger logger = Logger.getLogger("DEBUG");

  public static void main(String[] args) {
    // Set up logging to file...
    try {
      logger.addHandler(new FileHandler("debug.log", true));
    } catch (java.io.IOException e) {
      System.err.println("Can't log, quitting.");
      return;
    }

    // Parse flags... (--port=)
    String portFlag = maybeGetFlag(args, "port");
    int port = portFlag != null ? Integer.parseInt(portFlag) : 80;
    logger.info("Running on :" + port + "...");

    String clientUri = maybeGetFlag(args, "client_uri");
    if (clientUri == null) {
      clientUri = "http://localhost:3000";
    }

    String storeType = maybeGetFlag(args, "store");

    String clientPath = maybeGetFlag(args, "client_path");

    try {
      // Run Guice, and start the server it provides.
      Injector injector = Guice.createInjector(
          new ParserModule(),
          new BindingModule(clientPath),
          new ServerModule(port, clientUri, storeType)
      );

      // HACK - migrate in memory first
      // logger.info("Running test migration first...");
      // Migration m = new Migration();
      // m.loadData("113641087749801482038", injector.getInstance(Stores.class));
      // logger.info("Migration complete!");


      HttpServer server = injector.getInstance(HttpServer.class);
      System.out.println("\n*** Running server on :" + port + "...\n");
      server.start();
    } catch (Throwable t) {
      t.printStackTrace();
      throw t;
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
