package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ValueInsertRequest;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Action to return timeseries values for single lines, and add/remove them. */
public class SingleValuesHandler extends BaseValuesHandler {
  private final JsonParser<ValueInsertRequest> parser;

  @Inject SingleValuesHandler(JsonParser<ValueInsertRequest> parser) {
    this.parser = parser;
  }

  public JsonResponse handlePost(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String postData = IOUtils.toString(exchange.getRequestBody(), "utf-8");
    ValueInsertRequest request = parser.fromJson(postData);
    // TODO: parse into either 1 or many DateValues, insert into a store.
    return new JsonResponse("");
  }
}
