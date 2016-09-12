package today.useit.linetracker;

import today.useit.linetracker.json.JsonParser;
import today.useit.linetracker.json.JsonParserImpl;
import today.useit.linetracker.model.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

import java.util.List;

/** Binds JSON parsers for all the models required. */
public class ParserModule extends AbstractModule {
  @Override protected void configure() {
    // TODO - clean up, make one line instead of three for each.
    bind(new Key<JsonParser<SingleLineMeta>>(){})
      .toInstance(new JsonParserImpl<SingleLineMeta>(
        getProvider(Gson.class), new TypeToken<SingleLineMeta>(){}.getType()));
    bind(new Key<JsonParser<ComposLineMeta>>(){})
      .toInstance(new JsonParserImpl<ComposLineMeta>(
        getProvider(Gson.class), new TypeToken<ComposLineMeta>(){}.getType()));
    bind(new Key<JsonParser<GraphsLineMeta>>(){})
      .toInstance(new JsonParserImpl<GraphsLineMeta>(
        getProvider(Gson.class), new TypeToken<GraphsLineMeta>(){}.getType()));

    bind(new Key<JsonParser<List<SingleLineMeta>>>(){})
      .toInstance(new JsonParserImpl<List<SingleLineMeta>>(
        getProvider(Gson.class), new TypeToken<List<SingleLineMeta>>(){}.getType()));
    bind(new Key<JsonParser<List<ComposLineMeta>>>(){})
      .toInstance(new JsonParserImpl<List<ComposLineMeta>>(
        getProvider(Gson.class), new TypeToken<List<ComposLineMeta>>(){}.getType()));
    bind(new Key<JsonParser<List<GraphsLineMeta>>>(){})
      .toInstance(new JsonParserImpl<List<GraphsLineMeta>>(
        getProvider(Gson.class), new TypeToken<List<GraphsLineMeta>>(){}.getType()));

    bind(new Key<JsonParser<List<DatedValue>>>(){})
      .toInstance(new JsonParserImpl<List<DatedValue>>(
        getProvider(Gson.class), new TypeToken<List<DatedValue>>(){}.getType()));

    bind(new Key<JsonParser<ValueInsertRequest>>(){})
      .toInstance(new JsonParserImpl<ValueInsertRequest>(
        getProvider(Gson.class), new TypeToken<ValueInsertRequest>(){}.getType()));

    bind(new Key<JsonParser<EditChildrenRequest>>(){})
      .toInstance(new JsonParserImpl<EditChildrenRequest>(
        getProvider(Gson.class), new TypeToken<EditChildrenRequest>(){}.getType()));
  }
}
