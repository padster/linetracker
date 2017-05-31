package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.json.JsonParser;
import today.useit.linetracker.model.SingleLineMeta;
import today.useit.linetracker.store.Stores;

import javax.inject.Inject;

/** Action to generate JSONP to get one Single line. */
public class GetSingleHandler extends BaseItemHandler<SingleLineMeta> {
  @Inject GetSingleHandler(Stores stores, JsonParser<SingleLineMeta> parser) {
    super(stores.singleStore(), parser);
  }
}
