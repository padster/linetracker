package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.SingleLineMeta;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to list all the Single lines. */
public class ListSingleHandler implements Handler {
  private final JsonParser<List<SingleLineMeta>> parser;

  @Inject ListSingleHandler(JsonParser<List<SingleLineMeta>> parser) {
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    List<SingleLineMeta> lines = new ArrayList<>();
    SingleLineMeta line = new SingleLineMeta();
    line.id = "abcd1234";
    line.name = "hello 2";
    line.link = "http://www.example.com/working";
    lines.add(line);
    return new JsonResponse(parser.toJson(lines));
  }
}
