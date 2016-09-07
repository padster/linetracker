package today.useit.linetracker.handlers.data;

import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.SingleLineMeta;
import today.useit.linetracker.store.Stores;

import java.util.List;
import javax.inject.Inject;

/** Action to generate JSONP to list all the Single lines. */
public class ListSingleHandler extends BaseListHandler<SingleLineMeta> {
  @Inject ListSingleHandler(
    Stores stores,
    JsonParser<SingleLineMeta> itemParser,
    JsonParser<List<SingleLineMeta>> listParser
  ) {
    super(stores.singleStore(), itemParser, listParser);
  }
}
