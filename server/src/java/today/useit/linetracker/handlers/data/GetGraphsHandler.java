package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.GraphsLineMeta;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to get one Composite line. */
public class GetGraphsHandler implements Handler {
  private final JsonParser<GraphsLineMeta> parser;

  @Inject GetGraphsHandler(JsonParser<GraphsLineMeta> parser) {
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    // TODO: actual data...
    String id = pathDetails.get("id");

    // TODO: actual data..
    GraphsLineMeta line = new GraphsLineMeta();
    line.id = id;
    line.name = "hello graphs";
    line.childMetadata.add(new ChildEntry("childid1", "ch1"));
    line.childMetadata.add(new ChildEntry("childid2", "ch2"));
    return new JsonResponse(parser.toJson(line));
  }
}
