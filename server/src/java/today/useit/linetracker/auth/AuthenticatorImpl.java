package today.useit.linetracker.auth;

import java.net.URI;

import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.auth.AppAuthenticator;
import com.sun.net.httpserver.HttpPrincipal;

import jakarta.inject.Inject;

/** Implements Linetracker's Authenticator. */
public class AuthenticatorImpl implements AppAuthenticator {
  private static final String REALM = "linetracker";

  private final JwtUtil jwt;
  private final String clientUri;

  @Inject AuthenticatorImpl(JwtUtil jwt, @ClientUri String clientUri) {
    this.jwt = jwt;
    this.clientUri = clientUri;
  }

  @Override
  public URI buildLoginURI(String originalUrl) {
    return URI.create(this.clientUri + "/login?origin=" + originalUrl);
  }

  @Override
  public HttpPrincipal handleAuthToken(String token) {
    String userEmail = jwt.decodeJWTToEmail(token);
    return new HttpPrincipal(userEmail, REALM);
  }

  @Override
  public void handleNoUser() {
    // Ignore - no need to do anything...
  }
}
