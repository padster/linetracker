package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.json.JsonParser;
import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.store.Stores;

import jakarta.inject.Inject;

/** Action to generate JSONP to get one Composite line. */
@LoginRequired
public class GetComposHandler extends BaseItemHandler<ComposLineMeta> {
  @Inject GetComposHandler(Stores stores, JsonParser<ComposLineMeta> parser) {
    super(stores.composStore(), parser);
  }
}
