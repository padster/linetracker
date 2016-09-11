package today.useit.linetracker.store;

import today.useit.linetracker.model.DatedValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class InMemorySingleLineValuesStore implements SingleLineValuesStore {
  private final Map<String, SortedSet<DatedValue>> lines = new HashMap<>();

  public SortedSet<DatedValue> values(String id) {
    return this.lines.get(id);
  }

  public void addValues(String id, List<DatedValue> values) {
    SortedSet<DatedValue> line = this.values(id);
    if (line == null) {
      line = new TreeSet<DatedValue>();
      this.lines.put(id, line);
    }
    // Remove first so as to override values for dates already stored.
    line.removeAll(values);
    line.addAll(values);
  }

  public boolean removeValue(String id, String date) {
    return values(id).remove(new DatedValue(date, 0));
  }
}
