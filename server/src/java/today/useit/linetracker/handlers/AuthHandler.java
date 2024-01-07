package today.useit.linetracker.handlers;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.handlers.PostParser;
import com.github.padster.guiceserver.handlers.RouteHandlerResponses.RedirectResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sun.net.httpserver.HttpExchange;

import jakarta.inject.Inject;
import today.useit.linetracker.auth.JwtUtil;

/* Handle rewriting Google auth token with Linetracker's */
public class AuthHandler extends BaseCorsAwareHandler {
  /* Days to expire the login */
  private static final int LOGIN_EXPIRATION_DAYS = 30;

  private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
    new NetHttpTransport(), new GsonFactory()
  ).setAudience(Collections.singletonList("1050787416441-9j2g9lvtqllfbsaa1m2dj284hgsldpg8.apps.googleusercontent.com"))
  .build();

  private final JwtUtil jwt;

  @Inject AuthHandler(JwtUtil jwt, @ClientUri String clientUri) {
    super(clientUri);
    this.jwt = jwt;
  }

  @Override
  public Object handleInternal(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String method = exchange.getRequestMethod();
    if ("POST".equals(method)) {
      return this.handlePost(pathDetails, exchange);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public Object handlePost(Map<String, String> pathDetails, HttpExchange exchange) {
    Map<String, List<String>> bodyParams;
    try {
      bodyParams = PostParser.bodyAsParams(exchange);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Error(e);
    }

    String idTokenString = PostParser.forceSingle(bodyParams, "credential");

    String origin = "/";
    if (bodyParams.get("origin") != null) {
      List<String> originList = bodyParams.get("origin");
      if (originList.size() != 1 || !originList.get(0).startsWith("/")) {
        throw new IllegalArgumentException("Invalid login redirect origin");
      }
    }

    GoogleIdToken idToken = null;
    try {
      idToken = verifier.verify(idTokenString);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to verify google login token", e);
    }
    if (idToken == null) {
      throw new IllegalArgumentException("Invalid google login token");
    }

    String jwtToken = jwt.createJWT(
      idToken.getPayload().getEmail(), 
      1000 * 60 * 60 * 24 * LOGIN_EXPIRATION_DAYS
    );
    
    exchange.getResponseHeaders().add(
      "Set-Cookie", "_gsID=" + jwtToken + "; Path=/; Max-Age=" + (60 * 60 * 24 * LOGIN_EXPIRATION_DAYS)
    );

    try {
      return new RedirectResponse(new URI(this.clientUri + origin), true);
    } catch (Exception e) {
      throw new UnsupportedOperationException();
    }
  }
}
