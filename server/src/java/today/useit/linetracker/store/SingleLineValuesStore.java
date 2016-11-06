package today.useit.linetracker.store;

import today.useit.linetracker.model.DatedValue;

import java.util.List;
import java.util.SortedSet;

/** Fetches and modifies all dated values for a single line. */
public interface SingleLineValuesStore {
  /** @return all dated values for a line, in order. */
  SortedSet<DatedValue> values(String id);

  /** Adds a collection of new dated values, overwriting any old ones on provided dates. */
  void addValues(String id, List<DatedValue> values);

  /** Removes a single dated value from the line, by date. */
  boolean removeValue(String id, String date);
}
