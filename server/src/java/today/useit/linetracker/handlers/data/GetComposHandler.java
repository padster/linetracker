package today.useit.linetracker.handlers.data;

import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.store.Stores;

import javax.inject.Inject;

/** Action to generate JSONP to get one Composite line. */
public class GetComposHandler extends BaseItemHandler<ComposLineMeta> {
  @Inject GetComposHandler(Stores stores, JsonParser<ComposLineMeta> parser) {
    super(stores.composStore(), parser);
  }
}
