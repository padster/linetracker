package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.handlers.RouteHandlerResponses.JsonResponse;
import com.github.padster.guiceserver.json.JsonParser;

import today.useit.linetracker.handlers.BaseCorsAwareHandler;
import today.useit.linetracker.model.EditChildrenRequest;
import today.useit.linetracker.store.ChildStore;
import today.useit.linetracker.store.Stores;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.util.Map;
import jakarta.inject.Inject;

/** Modify (add or remove) the children of a line. */
@LoginRequired
public class EditChildrenHandler extends BaseCorsAwareHandler {
  private final ChildStore store;
  private final JsonParser<EditChildrenRequest> parser;

  @Inject EditChildrenHandler(Stores stores, JsonParser<EditChildrenRequest> parser) {
    this.store = stores.childStore();
    this.parser = parser;
  }

  @Override
  public JsonResponse handleInternal(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String method = exchange.getRequestMethod();
    if ("POST".equals(method)) {
      return this.handlePost(pathDetails, exchange);
    } else if ("OPTIONS".equals(method)) {
      return new JsonResponse("");
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public JsonResponse handlePost(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String type = pathDetails.get("type");
    String id = pathDetails.get("id");
    String fullID = type + "/" + id;
    String postData = IOUtils.toString(exchange.getRequestBody(), "utf-8");
    EditChildrenRequest req = this.parser.fromJson(postData);
    if (req.toRemove != null) {
      store.removeChild(fullID, req.toRemove);
    } else {
      store.addChildren(fullID, req.toAdd);
    }
    return new JsonResponse("");
  }
}
