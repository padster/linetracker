package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.handlers.RouteHandlerResponses.JsonResponse;
import com.github.padster.guiceserver.json.JsonParser;

import today.useit.linetracker.handlers.BaseCorsAwareHandler;
import today.useit.linetracker.store.ItemStore;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

/** Action to generate JSONP to get one Composite line. */
public abstract class BaseItemHandler<T> extends BaseCorsAwareHandler {
  protected final ItemStore<T> itemStore;
  protected final JsonParser<T> parser;

  protected BaseItemHandler(ItemStore<T> itemStore, JsonParser<T> parser, String clientUri) {
    super(clientUri);
    this.itemStore = itemStore;
    this.parser = parser;
  }

  @Override
  public JsonResponse handleInternal(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String method = exchange.getRequestMethod();
    if ("GET".equals(method)) {
      return this.handleGet(pathDetails, exchange);
    } else if ("DELETE".equals(method)) {
      return this.handleDelete(pathDetails, exchange);
    } else if ("OPTIONS".equals(method)) {
      return new JsonResponse("");
    } else {
      throw new UnsupportedOperationException();
    }
  }

  /** @return ID'd item fetched from store, as json. */
  public JsonResponse handleGet(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    T item = itemStore.getItem(id);
    if (item == null) {
      throw new java.io.FileNotFoundException();
    }
    return new JsonResponse(parser.toJson(item));
  }

  public JsonResponse handleDelete(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    boolean result = itemStore.deleteItem(id);
    return new JsonResponse(Boolean.toString(result));
  }
}
