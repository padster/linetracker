package today.useit.linetracker.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

/** Generic action that handles an HTTP request at a given path. */
public interface Action {
  Object handle(Map<String, String> pathDetails, HttpExchange exchange) throws Exception;
}
