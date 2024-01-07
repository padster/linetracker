package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.json.JsonParser;
import today.useit.linetracker.model.GraphsLineMeta;
import today.useit.linetracker.store.Stores;

import java.util.List;
import jakarta.inject.Inject;

/** Action to generate JSONP to list all the Graphs. */
@LoginRequired
public class ListGraphsHandler extends BaseListHandler<GraphsLineMeta> {
  @Inject ListGraphsHandler(
    Stores stores,
    JsonParser<GraphsLineMeta> itemParser,
    JsonParser<List<GraphsLineMeta>> listParser,
    @ClientUri String clientUri
  ) {
    super(stores.graphsStore(), itemParser, listParser, clientUri);
  }
}
