package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.ComposLineMeta;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to get one Composite line. */
public class GetComposHandler implements Handler {
  private final JsonParser<ComposLineMeta> parser;

  @Inject GetComposHandler(JsonParser<ComposLineMeta> parser) {
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    // TODO: actual data...
    String id = pathDetails.get("id");

    // TODO: actual data..
    ComposLineMeta line = new ComposLineMeta();
    line.id = id;
    line.name = "hello compos";
    line.childMetadata.add(new ChildEntry("childid1", "ch1"));
    line.childMetadata.add(new ChildEntry("childid2", "ch2"));
    return new JsonResponse(parser.toJson(line));
  }
}
