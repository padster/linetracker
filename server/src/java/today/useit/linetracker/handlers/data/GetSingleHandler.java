package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

/** Action to generate JSONP to get one Single line. */
public class GetSingleHandler implements Handler {
  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    // TODO: actual data..
    String id = pathDetails.get("id");
    return new JsonResponse(String.format(
      "{\"id\": \"%s\", \"name\": \"hello\", \"link\": \"http://www.example.com\"}", id
    ));
  }
}
