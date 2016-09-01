package today.useit.linetracker.json;

/** Tool for a bidirectional Object <-> JSON String mapping. */
public interface JsonParser<T> {
  T fromJson(String json);
  String toJson(T value);
}
