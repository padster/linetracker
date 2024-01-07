package today.useit.linetracker.handlers.data;

import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.handlers.RouteHandlerResponses.JsonResponse;
import com.github.padster.guiceserver.json.JsonParser;
import com.sun.net.httpserver.HttpExchange;

import jakarta.inject.Inject;
import today.useit.linetracker.handlers.BaseCorsAwareHandler;
import today.useit.linetracker.model.Settings;
import today.useit.linetracker.store.Stores;

/** Action to return timeseries values for lines, and add/remove them. */
@LoginRequired
public class SettingsHandler extends BaseCorsAwareHandler {
  private final JsonParser<Settings> settingsParser;
  private final Stores stores;

  @Inject SettingsHandler(
    Stores stores,
    JsonParser<Settings> settingsParser,
    @ClientUri String clientUri
  ) {
    super(clientUri);
    this.stores = stores;
    this.settingsParser = settingsParser;
  }

  @Override
  public JsonResponse handleInternal(Map<String, String> pathDetails, HttpExchange exchange)
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
