package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.ComposLineMeta;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to list all the Composite lines. */
public class ListComposHandler implements Handler {
  private final JsonParser<List<ComposLineMeta>> parser;

  @Inject ListComposHandler(JsonParser<List<ComposLineMeta>> parser) {
    this.parser = parser;
  }
  
  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    List<ComposLineMeta> lines = new ArrayList<>();
    ComposLineMeta line = new ComposLineMeta();
    line.id = "1a2b3c4d";
    line.name = "lines";
    lines.add(line);
    return new JsonResponse(parser.toJson(lines));
  }
}
