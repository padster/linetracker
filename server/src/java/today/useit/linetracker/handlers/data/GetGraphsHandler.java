package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.auth.AuthAnnotations.LoginRequired;
import com.github.padster.guiceserver.json.JsonParser;
import today.useit.linetracker.model.GraphsLineMeta;
import today.useit.linetracker.store.Stores;

import jakarta.inject.Inject;


/** Action to generate JSONP to get one Graph. */
@LoginRequired
public class GetGraphsHandler extends BaseItemHandler<GraphsLineMeta> {
  @Inject GetGraphsHandler(Stores stores, JsonParser<GraphsLineMeta> parser) {
    super(stores.graphsStore(), parser);
  }
}
