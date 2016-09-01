package today.useit.linetracker.json;

import com.google.gson.Gson;

import javax.inject.Provider;

/**
 * Provide mappings to and from JSON for a particular class, using Gson.
 */
public class JsonParserImpl<T> implements JsonParser<T> {
  public final Provider<Gson> gsonProvider;
  public final Class<T> clazz;

  public JsonParserImpl(Provider<Gson> gsonProvider, Class<T> clazz) {
    this.gsonProvider= gsonProvider;
    this.clazz = clazz;
  }

  @Override public T fromJson(String json) {
    return gson().fromJson(json, clazz);
  }

  @Override public String toJson(T value) {
    return gson().toJson(value);
  }

  private Gson gson() {
    return gsonProvider.get();
  }
}
