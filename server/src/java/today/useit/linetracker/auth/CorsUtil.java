package today.useit.linetracker.auth;

import com.sun.net.httpserver.HttpExchange;

public class CorsUtil {
  public static void addCorsHeaders(HttpExchange exchange, String clientUri) {
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", clientUri);
    exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
  }
}
