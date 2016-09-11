package today.useit.linetracker.store;

import today.useit.linetracker.model.DatedValue;

import java.util.List;
import java.util.SortedSet;

public interface SingleLineValuesStore {
  SortedSet<DatedValue> values(String id);

  void addValues(String id, List<DatedValue> values);

  boolean removeValue(String id, String date);
}
