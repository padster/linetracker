package today.useit.linetracker;

import com.github.padster.guiceserver.handlers.RouteHandler;
import com.github.padster.guiceserver.Annotations.Bindings;
import com.github.padster.guiceserver.Annotations.ServerPort;
import com.github.padster.guiceserver.Annotations.CurrentUser;
import today.useit.linetracker.store.cloud.CloudStores;
import today.useit.linetracker.store.memory.InMemoryStores;
import today.useit.linetracker.store.Stores;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import redis.clients.jedis.Jedis;

import javax.inject.Provider;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.function.BiConsumer;

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
      Datastore db = DatastoreOptions.defaultInstance().service();
      bind(Datastore.class).toInstance(db);
      bind(Stores.class).to(CloudStores.class).asEagerSingleton();
      // bind(Stores.class).toInstance(new CloudStores(db));
    } else if ("datastore_gcp".equals(storeType)) {
      System.out.println("> Storage: GCP Datastore. TODO: implement");
    } else {
      throw new IllegalArgumentException("Unknown store: " + storeType);
    }

    // TODO: properly
    bind(String.class).annotatedWith(CurrentUser.class).toInstance("HACK");
  }

  @Provides @Singleton
  HttpServer provideServer(
    @ServerPort int serverPort,
    RouteHandler routeHandler
  ) throws IOException {
    try {
      // Create a server, and route every request through the base handler.
      HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
      server.createContext("/", routeHandler);
      server.setExecutor(null); // Default executor
      return server;
    } catch (Throwable t) {
      t.printStackTrace();
      throw t;
    }
  }
}
