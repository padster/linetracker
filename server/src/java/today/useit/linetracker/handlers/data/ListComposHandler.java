package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.json.JsonParser;
import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.store.Stores;

import java.util.List;
import jakarta.inject.Inject;

/** Action to generate JSONP to list all the Composite lines. */
@LoginRequired
public class ListComposHandler extends BaseListHandler<ComposLineMeta> {
  @Inject ListComposHandler(
    Stores stores,
    JsonParser<ComposLineMeta> itemParser,
    JsonParser<List<ComposLineMeta>> listParser
  ) {
    super(stores.composStore(), itemParser, listParser);
  }
}
