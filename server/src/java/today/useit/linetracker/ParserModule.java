package today.useit.linetracker;

import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.json.JsonParserImpl;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

/** Binds JSON parsers for all the models required. */
public class ParserModule extends AbstractModule {
  @Override protected void configure() {
    // TODO: Add here as models are needed.
    // bind(new Key<JsonParser<:ModelType:>>(){})
    //     .toInstance(new JsonParserImpl<>(getProvider(Gson.class), :ModelType:.class));
  }
}
