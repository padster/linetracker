package today.useit.linetracker.auth;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Utility class that creates JWTs and decodes them back.
 */
public class JwtUtil {
  private static final String ISSUER = "today.useit.linetracker";

  // Key for signing JWTs.
  private final SecretKey JST_KEY;

  @Inject JwtUtil() {
    // Build the key once:
    try {
      String encodedKeyString = new String(Files.readAllBytes(Paths.get("secret.key")));
      byte[] encodedKeyBytes = Base64.getDecoder().decode(encodedKeyString);
      JST_KEY = new SecretKeySpec(encodedKeyBytes, 0, encodedKeyBytes.length, "HmacSHA256");
    } catch (Exception e) {
      System.out.println("Error creating key: " + e);
      System.out.flush();
      throw new IllegalStateException(e);
    }
  }

  /** Create a JWT string for the given user, with optional expiry time. */
  public String createJWT(String userEmail, long ttlMillis) {
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    try {
      JwtBuilder builder = Jwts.builder()
        .id("1234")
        .issuedAt(now)
        .claim("email", userEmail)
        .issuer(ISSUER)
        .signWith(JST_KEY);

      // If it has been specified, let's add the expiration
      if (ttlMillis > 0) {
          builder = builder.expiration(new Date(nowMillis + ttlMillis));
      }  
    
      return builder.compact();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  /** Decode and validate a JWT string, and extract the user email. */
  public String decodeJWTToEmail(String jwt) {
    try {
      Claims claims = Jwts.parser()
              .requireIssuer(ISSUER)
              .verifyWith(JST_KEY)
              .build()
              .parseSignedClaims(jwt)
              .getPayload();
      return claims.get("email", String.class);
    } catch (Exception e) {
      System.out.println("Error decoding: " + e);
      System.out.flush();
      throw new IllegalStateException(e);
    }
  }
}
