package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.store.Store;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to get one Composite line. */
public class GetComposHandler implements Handler {
  private final Store store;
  private final JsonParser<ComposLineMeta> parser;

  @Inject GetComposHandler(Store store, JsonParser<ComposLineMeta> parser) {
    this.store = store;
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    ComposLineMeta line = store.getComposMeta(id);
    if (line == null) {
      throw new java.io.FileNotFoundException();
    }
    return new JsonResponse(parser.toJson(line));
  }
}
