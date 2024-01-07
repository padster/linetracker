package today.useit.linetracker.handlers;

import today.useit.linetracker.Annotations.ClientPath;

import com.github.padster.guiceserver.handlers.Handler;
import com.github.padster.guiceserver.handlers.RouteHandlerResponses.TextResponse;

import com.sun.net.httpserver.HttpExchange;

import jakarta.inject.Inject;
import java.util.Map;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * Present a test page. TODO: delete, or convert to proper debug.
 */
public class StaticHandler implements Handler {
  private final String clientPath;

  @Inject StaticHandler(@ClientPath String clientPath) {
    this.clientPath = clientPath;
  }

  @Override public Object handle(Map<String, String> pathDetails, HttpExchange exchange) {
    if (!"GET".equals(exchange.getRequestMethod())) {
      throw new UnsupportedOperationException("Can only GET from DebugHandler");
    }

    String path = exchange.getRequestURI().getPath();
    String gcsBucket = "linetracking_static";
    String gcsPath = this.clientPath + path;
    System.out.println("TODO: HANDLE " + gcsPath);

    // Storage storage = StorageOptions.newBuilder().setProjectId("linetracking").build().getService();

    Storage storage = StorageOptions.newBuilder().build().getService();
    int bufSize = 2000;
    String content = null;
    try (
      ReadChannel channel = storage.reader(BlobId.of(gcsBucket, gcsPath));
    ) {
      char[] ca = new char[bufSize];

      ByteBuffer bb = ByteBuffer.allocate(bufSize);
      StringBuilder sb = new StringBuilder();
      while(channel.read(bb) > -1) {
          CharBuffer cb = bb.asCharBuffer();
          cb.flip();
          cb.get(ca);
          sb.append(ca);
          cb.clear();
      }
      content = sb.toString();
    } catch (java.io.IOException e) {
      
    }

    // String fileContent = new String(out.toByteArray(), StandardCharsets.UTF_8);
    return new TextResponse(content);
  }
}
