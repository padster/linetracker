package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.GraphsLineMeta;
import today.useit.linetracker.store.Store;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to get one Composite line. */
public class GetGraphsHandler implements Handler {
  private final Store store;
  private final JsonParser<GraphsLineMeta> parser;

  @Inject GetGraphsHandler(Store store, JsonParser<GraphsLineMeta> parser) {
    this.store = store;
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    // TODO - properly split out method type handling into superclass
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

  public JsonResponse handleGet(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    GraphsLineMeta line = store.getGraphsMeta(id);
    if (line == null) {
      throw new java.io.FileNotFoundException();
    }
    return new JsonResponse(parser.toJson(line));
  }

  public JsonResponse handleDelete(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    boolean result = store.deleteGraphsMeta(id);
    return new JsonResponse(Boolean.toString(result));
  }
}
