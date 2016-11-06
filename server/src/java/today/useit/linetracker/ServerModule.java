package today.useit.linetracker;

import today.useit.linetracker.BindingModule.Bindings;
import today.useit.linetracker.BindingModule.ServerPort;
import today.useit.linetracker.handlers.RouteHandler;
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
  public final boolean cloudStore;

  public ServerModule(int port, boolean cloudStore) {
    this.port = port;
    this.cloudStore = cloudStore;
  }

  @Override protected void configure() {
    bind(Integer.class).annotatedWith(ServerPort.class).toInstance(this.port);

    if (cloudStore) {
      Datastore db = DatastoreOptions.defaultInstance().service();
      bind(Datastore.class).toInstance(db);
      bind(Stores.class).to(CloudStores.class).asEagerSingleton();
      // bind(Stores.class).toInstance(new CloudStores(db));
    } else {
      bind(Stores.class).to(InMemoryStores.class).asEagerSingleton();
    }
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
