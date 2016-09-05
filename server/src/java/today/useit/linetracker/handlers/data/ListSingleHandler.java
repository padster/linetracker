package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.SingleLineMeta;
import today.useit.linetracker.store.Store;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to list all the Single lines. */
public class ListSingleHandler implements Handler {
  private final Store store;
  private final JsonParser<List<SingleLineMeta>> parser;

  @Inject ListSingleHandler(Store store, JsonParser<List<SingleLineMeta>> parser) {
    this.store = store;
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    List<SingleLineMeta> lines = store.listSingleMeta();
    return new JsonResponse(parser.toJson(lines));
  }
}
