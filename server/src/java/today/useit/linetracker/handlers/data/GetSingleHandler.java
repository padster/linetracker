package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.SingleLineMeta;
import today.useit.linetracker.store.Store;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to get one Single line. */
public class GetSingleHandler implements Handler {
  private final Store store;
  private final JsonParser<SingleLineMeta> parser;

  @Inject GetSingleHandler(Store store, JsonParser<SingleLineMeta> parser) {
    this.store = store;
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    SingleLineMeta line = store.getSingleMeta(id);
    if (line == null) {
      throw new java.io.FileNotFoundException();
    }
    return new JsonResponse(parser.toJson(line));
  }
}
