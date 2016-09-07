package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.GraphsLineMeta;
import today.useit.linetracker.store.Stores;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to list all the Single lines. */
public class ListGraphsHandler implements Handler {
  private final Stores stores;
  private final JsonParser<List<GraphsLineMeta>> parser;

  @Inject ListGraphsHandler(Stores stores, JsonParser<List<GraphsLineMeta>> parser) {
    this.stores = stores;
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    List<GraphsLineMeta> lines = stores.graphsStore().listItems();
    return new JsonResponse(parser.toJson(lines));
  }
}
