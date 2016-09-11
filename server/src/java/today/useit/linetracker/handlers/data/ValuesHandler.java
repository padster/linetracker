package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.DatedValue;
import today.useit.linetracker.model.ValueInsertRequest;
import today.useit.linetracker.store.Stores;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;


/** Action to return timeseries values for lines, and add/remove them. */
public class ValuesHandler implements Handler {
  private final JsonParser<List<DatedValue>> valuesParser;
  private final JsonParser<ValueInsertRequest> insertParser;
  private final Stores stores;

  @Inject ValuesHandler(
    Stores stores,
    JsonParser<List<DatedValue>> valuesParser,
    JsonParser<ValueInsertRequest> insertParser
  ) {
    this.stores = stores;
    this.valuesParser = valuesParser;
    this.insertParser = insertParser;
  }

  public JsonResponse handle(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String method = exchange.getRequestMethod();
    if ("GET".equals(method)) {
      return this.handleGet(pathDetails, exchange);
    } else if ("POST".equals(method)) {
      return this.handlePost(pathDetails, exchange);
    } else if ("OPTIONS".equals(method)) {
      return new JsonResponse("");
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public JsonResponse handleGet(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    String type = pathDetails.get("type");
    System.out.println("Getting for " + id + " of " + type);

    final List<DatedValue> result;
    if ("single".equals(type)) {
      result = this.stores.valuesStore().valuesForSingle(id);
    } else if ("compos".equals(type)) {
      result = this.stores.valuesStore().valuesForCompos(id);
    } else {
      throw new FileNotFoundException();
    }

    return new JsonResponse(valuesParser.toJson(result));
  }

  public JsonResponse handlePost(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String id = pathDetails.get("id");
    String type = pathDetails.get("type");
    System.out.println("CHANGING for " + id + " - " + type);

    String postData = IOUtils.toString(exchange.getRequestBody(), "utf-8");
    ValueInsertRequest request = insertParser.fromJson(postData);
    // TODO: parse into either 1 or many DateValues, insert into a store.
    return new JsonResponse("");
  }
}