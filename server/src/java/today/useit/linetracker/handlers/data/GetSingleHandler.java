package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.SingleLineMeta;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import javax.inject.Inject;

/** Action to generate JSONP to get one Single line. */
public class GetSingleHandler implements Handler {
  private final JsonParser<SingleLineMeta> parser;

  @Inject GetSingleHandler(JsonParser<SingleLineMeta> parser) {
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");

    // TODO: actual data..
    SingleLineMeta line = new SingleLineMeta();
    line.id = id;
    line.name = "hello 2";
    line.link = "http://www.example.com/working";
    return new JsonResponse(parser.toJson(line));
  }
}
