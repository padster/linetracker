package today.useit.linetracker.handlers.data;

import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.model.GraphsLineMeta;
import today.useit.linetracker.store.Stores;

import javax.inject.Inject;

/** Action to generate JSONP to get one Graph. */
public class GetGraphsHandler extends BaseItemHandler<GraphsLineMeta> {
  @Inject GetGraphsHandler(Stores stores, JsonParser<GraphsLineMeta> parser) {
    super(stores.graphsStore(), parser);
  }
}
