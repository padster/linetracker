package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.json.JsonParser;
import today.useit.linetracker.model.SingleLineMeta;
import today.useit.linetracker.store.Stores;

import java.util.List;
import jakarta.inject.Inject;

/** Action to generate JSONP to list all the Single lines. */
@LoginRequired
public class ListSingleHandler extends BaseListHandler<SingleLineMeta> {
  @Inject ListSingleHandler(
    Stores stores,
    JsonParser<SingleLineMeta> itemParser,
    JsonParser<List<SingleLineMeta>> listParser,
    @ClientUri String clientUri
  ) {
    super(stores.singleStore(), itemParser, listParser, clientUri);
  }
}
