package today.useit.linetracker.handlers.data;

import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.store.Stores;

import java.util.List;
import javax.inject.Inject;

/** Action to generate JSONP to list all the Composite lines. */
public class ListComposHandler extends BaseListHandler<ComposLineMeta> {
  @Inject ListComposHandler(
    Stores stores,
    JsonParser<ComposLineMeta> itemParser,
    JsonParser<List<ComposLineMeta>> listParser
  ) {
    super(stores.composStore(), itemParser, listParser);
  }
}
