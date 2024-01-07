package today.useit.linetracker;

import com.github.padster.guiceserver.auth.RouteAuthenticator;
import com.github.padster.guiceserver.handlers.RouteHandler;
import com.github.padster.guiceserver.Annotations.ServerPort;
import today.useit.linetracker.store.cloud.CloudStores;
import today.useit.linetracker.store.memory.InMemoryStores;
import today.useit.linetracker.auth.JwtUtil;
import today.useit.linetracker.store.Stores;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/** Configures the HttpServer within Guice. */
public class ServerModule extends AbstractModule {
  public final int port;
  public final String storeType;

  public ServerModule(int port, String storeType) {
    this.port = port;
    this.storeType = storeType;
  }

  @Override protected void configure() {
    bind(Integer.class).annotatedWith(ServerPort.class).toInstance(this.port);

    if (this.storeType == null) {
      System.out.println("> Storage: In memory");
      bind(Stores.class).to(InMemoryStores.class).asEagerSingleton();
    } else if ("datastore_local".equals(storeType)) {
      System.out.println("> Storage: Local Datastore.");

      System.out.println(String.format("\nUsing storage at: %s",
        System.getenv().get("DATASTORE_EMULATOR_HOST")));
      if ("".equals(System.getenv().get("DATASTORE_EMULATOR_HOST"))) {
        throw new IllegalArgumentException(
          "Must use the datastore emulator for now...\n" +
          "run: $(gcloud beta emulators datastore env-init --data-dir=cloudstore)");
      }
      Datastore db = DatastoreOptions.getDefaultInstance().getService();
      bind(Datastore.class).toInstance(db);
      bind(Stores.class).to(CloudStores.class).asEagerSingleton();
      // bind(Stores.class).toInstance(new CloudStores(db));
    } else if ("datastore_gcp".equals(storeType)) {
      Datastore db = DatastoreOptions.newBuilder()
        .setProjectId("useful-theory-217216")
        .setDatabaseId("linetracker")
        .build().getService();
      bind(Datastore.class).toInstance(db);
      bind(Stores.class).to(CloudStores.class).asEagerSingleton();
    } else {
      throw new IllegalArgumentException("Unknown store: " + storeType);
    }

    bind(JwtUtil.class).asEagerSingleton();
  }

  @Provides @Singleton
  HttpServer provideServer(
    @ServerPort int serverPort,
    RouteHandler routeHandler,
    RouteAuthenticator routeAuthenticator
  ) throws IOException {
    try {
      // Create a server, and route every request through the base handler.
      HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
      server.createContext("/", routeHandler).setAuthenticator(routeAuthenticator);
      server.setExecutor(null); // Default executor
      return server;
    } catch (Throwable t) {
      t.printStackTrace();
      throw t;
    }
  }
}
