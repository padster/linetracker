package today.useit.linetracker.handlers;

import java.util.Map;

import com.github.padster.guiceserver.handlers.Handler;
import com.sun.net.httpserver.HttpExchange;

import today.useit.linetracker.auth.CorsUtil;

/** Handler that adds CORS headers after handling. */
public abstract class BaseCorsAwareHandler implements Handler {
  // TODO - Do via annotations and post-processing instead.

  protected final String clientUri;

  protected BaseCorsAwareHandler(String clientUri) {
    this.clientUri = clientUri;
  }

  @Override
  public Object handle(Map<String, String> pathDetails, HttpExchange exchange) throws Exception {
    Object result = this.handleInternal(pathDetails, exchange);
    // If the uri contains 'localhost', add the header:
    if (this.clientUri.contains("localhost")) {
      CorsUtil.addCorsHeaders(exchange, this.clientUri);
    }
    return result;
  }

  public abstract Object handleInternal(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception;
  
}
