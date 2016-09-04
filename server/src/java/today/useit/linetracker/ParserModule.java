package today.useit.linetracker;

import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.json.JsonParserImpl;
import today.useit.linetracker.model.*;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

/** Binds JSON parsers for all the models required. */
public class ParserModule extends AbstractModule {
  @Override protected void configure() {
    bind(new Key<JsonParser<SingleLineMeta>>(){})
        .toInstance(new JsonParserImpl<>(getProvider(Gson.class), SingleLineMeta.class));
    bind(new Key<JsonParser<ComposLineMeta>>(){})
        .toInstance(new JsonParserImpl<>(getProvider(Gson.class), ComposLineMeta.class));
    bind(new Key<JsonParser<GraphsLineMeta>>(){})
        .toInstance(new JsonParserImpl<>(getProvider(Gson.class), GraphsLineMeta.class));

  }
}
