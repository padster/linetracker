package today.useit.linetracker.handlers.data;

import today.useit.linetracker.handlers.Handler;
import today.useit.linetracker.handlers.RouteHandlerResponses.JsonResponse;
import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.Settings;
import today.useit.linetracker.store.Stores;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/** Action to return timeseries values for lines, and add/remove them. */
public class SettingsHandler implements Handler {
  private final JsonParser<Settings> settingsParser;
  private final Stores stores;

  @Inject SettingsHandler(
    Stores stores,
    JsonParser<Settings> settingsParser
  ) {
    this.stores = stores;
    this.settingsParser = settingsParser;
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
    Settings settings = this.stores.settingsStore().getSettings();
   return new JsonResponse(settingsParser.toJson(settings));
  }

  public JsonResponse handlePost(Map<String, String> pathDetails, HttpExchange exchange)
      throws Exception {
    String postData = IOUtils.toString(exchange.getRequestBody(), "utf-8");
    Settings settings = settingsParser.fromJson(postData);
    this.stores.settingsStore().updateSettings(settings);
    return new JsonResponse(settingsParser.toJson(settings));
  }
}
