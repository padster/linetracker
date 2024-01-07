package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.handlers.RouteHandlerResponses.JsonResponse;

import today.useit.linetracker.handlers.BaseCorsAwareHandler;
import today.useit.linetracker.store.Stores;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import jakarta.inject.Inject;

/** Handler which is used to remove a single dated value from a line. */
@LoginRequired
public class DatedValueHandler extends BaseCorsAwareHandler {
  private final Stores stores;

  @Inject DatedValueHandler(Stores stores, @ClientUri String clientUri) {
    super(clientUri);
    this.stores = stores;
  }

  @Override
  public JsonResponse handleInternal(Map<String, String> pathDetails, HttpExchange exchange)
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
