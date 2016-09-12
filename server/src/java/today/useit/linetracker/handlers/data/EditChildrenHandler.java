package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.EditChildrenRequest;
import today.useit.linetracker.store.ChildStore;
import today.useit.linetracker.store.Stores;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Modify (add or remove) the children of a line. */
public class EditChildrenHandler implements Handler {
  private final ChildStore store;
  private final JsonParser<EditChildrenRequest> parser;

  @Inject EditChildrenHandler(Stores stores, JsonParser<EditChildrenRequest> parser) {
    this.store = stores.childStore();
    this.parser = parser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
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
