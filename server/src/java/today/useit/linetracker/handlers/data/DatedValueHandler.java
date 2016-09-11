package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.store.Stores;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import javax.inject.Inject;

/** Handler which is used to remove a single dated value from a line. */
public class DatedValueHandler implements Handler {
  private final Stores stores;

  @Inject DatedValueHandler(Stores stores) {
    this.stores = stores;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String method = exchange.getRequestMethod();
    if ("DELETE".equals(method)) {
      return this.handleDelete(pathDetails, exchange);
    } else if ("OPTIONS".equals(method)) {
      return new JsonResponse("");
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public JsonResponse handleDelete(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    String yyyymmdd = pathDetails.get("yyyymmdd");
    boolean result = stores.valuesStore().removeValueFromSingleLine(id, yyyymmdd);
    return new JsonResponse(Boolean.toString(result));
  }
}
