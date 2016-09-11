package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.store.ItemStore;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to list all lines. and create new ones */
public abstract class BaseListHandler<T> implements Handler {
  private final ItemStore<T> itemStore;
  private final JsonParser<T> itemParser;
  private final JsonParser<List<T>> listParser;

  BaseListHandler(
    ItemStore<T> itemStore,
    JsonParser<T> itemParser,
    JsonParser<List<T>> listParser
  ) {
    this.itemStore = itemStore;
    this.itemParser = itemParser;
    this.listParser = listParser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
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
    T result = itemStore.createItem(itemParser.fromJson(postData));
    return new JsonResponse(itemParser.toJson(result));
  }
}