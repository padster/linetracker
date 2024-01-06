package today.useit.linetracker.auth;

import com.sun.net.httpserver.HttpExchange;

public class CorsUtil {
  public static void addCorsHeaders(HttpExchange exchange) {
    // TODO - only set when different port
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
    exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
  }
}
