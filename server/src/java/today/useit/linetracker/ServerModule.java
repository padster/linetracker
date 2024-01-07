package today.useit.linetracker;

import com.github.padster.guiceserver.auth.RouteAuthenticator;
import com.github.padster.guiceserver.handlers.RouteHandler;
import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.Annotations.ServerPort;
import today.useit.linetracker.store.cloud.CloudStores;
import today.useit.linetracker.store.memory.InMemoryStores;
import today.useit.linetracker.auth.JwtUtil;
import today.useit.linetracker.store.Stores;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

/** Configures the HttpServer within Guice. */
public class ServerModule extends AbstractModule {
  public final int port;
  public final String clientUri;
  public final String storeType;

  public ServerModule(int port, String clientUri, String storeType) {
    this.port = port;
    this.clientUri = clientUri;
    this.storeType = storeType;
  }

  @Override protected void configure() {
    bind(Integer.class).annotatedWith(ServerPort.class).toInstance(this.port);
    bind(String.class).annotatedWith(ClientUri.class).toInstance(this.clientUri);

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
      // Load credentials from lts-sa.json:
      String credFile = System.getenv().get("GOOGLE_APPLICATION_CREDENTIALS");
      System.out.println("> Load creds from " + Paths.get(credFile));
      try {
        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(
          Files.newInputStream(Paths.get(credFile))
        );

        Datastore db = DatastoreOptions.newBuilder()
          .setProjectId("useful-theory-217216")
          .setDatabaseId("linetracker")
          .setCredentials(credentials)
          .build().getService();
        bind(Datastore.class).toInstance(db);
        bind(Stores.class).to(CloudStores.class).asEagerSingleton();
      } catch (IOException e) {
        e.printStackTrace();
        throw new IllegalArgumentException("Could not load SA credentials", e);
      }

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
