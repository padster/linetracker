package today.useit.linetracker.handlers.data;

import com.github.padster.guiceserver.Annotations.ClientUri;
import com.github.padster.guiceserver.json.JsonParser;
import today.useit.linetracker.model.GraphsLineMeta;
import today.useit.linetracker.store.Stores;

import jakarta.inject.Inject;


/** Action to generate JSONP to get one Graph. */
public class GetGraphsHandler extends BaseItemHandler<GraphsLineMeta> {
  @Inject GetGraphsHandler(
    Stores stores, 
    JsonParser<GraphsLineMeta> parser,
    @ClientUri String clientUri
  ) {
    super(stores.graphsStore(), parser, clientUri);
  }
}
