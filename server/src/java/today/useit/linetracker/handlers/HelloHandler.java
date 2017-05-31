package today.useit.linetracker.handlers;

import com.github.padster.guiceserver.handlers.Handler;
import com.github.padster.guiceserver.handlers.RouteHandlerResponses.MustacheResponse;

import com.sun.net.httpserver.HttpExchange;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Present a test page. TODO: delete, or convert to proper debug.
 */
public class HelloHandler implements Handler {
  @Override public Object handle(Map<String, String> pathDetails, HttpExchange exchange) {
    if (!"GET".equals(exchange.getRequestMethod())) {
      throw new UnsupportedOperationException("Can only GET from DebugHandler");
    }

    // Execute the template.
    Map<String, Object> params = new HashMap<>();
    params.put("key", "hello");
    params.put("value", true);
    return new MustacheResponse("hello.template", params);
  }
}
