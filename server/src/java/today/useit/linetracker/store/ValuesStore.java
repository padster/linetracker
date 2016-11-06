package today.useit.linetracker.store;

import today.useit.linetracker.model.DatedValue;

import java.util.List;

/** Storage for the dated values attached to each line. */
public interface ValuesStore {
  /** @return All dated values for a single line, in order. */
  List<DatedValue> valuesForSingle(String id);

  /** @return All dated values for a composite line, in order. */
  List<DatedValue> valuesForCompos(String id);

  /** Insert a new dated value for a single line, overwriting any old one on the same date. */
  void addValuesToSingleLine(String id, List<DatedValue> values);

  /** Remove a value at a given date for a single line. */
  boolean removeValueFromSingleLine(String id, String date);
}
