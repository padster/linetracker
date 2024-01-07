package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.handlers.RouteHandlerResponses.JsonResponse;
import com.github.padster.guiceserver.json.JsonParser;

import today.useit.linetracker.handlers.BaseCorsAwareHandler;
import today.useit.linetracker.store.ItemStore;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.Map;

/** Action to generate JSONP to list all lines. and create new ones */
public abstract class BaseListHandler<T> extends BaseCorsAwareHandler {
  private final ItemStore<T> itemStore;
  private final JsonParser<T> itemParser;
  private final JsonParser<List<T>> listParser;

  protected BaseListHandler(
    ItemStore<T> itemStore,
    JsonParser<T> itemParser,
    JsonParser<List<T>> listParser,
    String clientUri
  ) {
    super(clientUri);
    this.itemStore = itemStore;
    this.itemParser = itemParser;
    this.listParser = listParser;
  }

  @Override
  public JsonResponse handleInternal(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String method = exchange.getRequestMethod();
    if ("GET".equals(method)) {
      return this.handleGet(pathDetails, exchange);
    } else if ("POST".equals(method)) {
      return this.handlePost(pathDetails, exchange);
    } else if ("OPTIONS".equals(method)) {
      return new JsonResponse("");
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public JsonResponse handleGet(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    List<T> items = itemStore.listItems();
    return new JsonResponse(listParser.toJson(items));
  }

  public JsonResponse handlePost(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String postData = IOUtils.toString(exchange.getRequestBody(), "utf-8");
    System.out.println("Creating in " + itemStore.getClass().getName());
    T result = itemStore.createItem(itemParser.fromJson(postData));
    return new JsonResponse(itemParser.toJson(result));
  }
}
