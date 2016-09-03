package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

/** Action to generate JSONP to list all the Composite lines. */
public class ListComposHandler implements Handler {
  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    // TODO: actual data..
    return new JsonResponse(
      "[{name: 'hello', id: '1a2b3c4d'}]"
    );
  }
}
