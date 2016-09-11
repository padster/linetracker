package today.useit.linetracker.store;

import today.useit.linetracker.model.DatedValue;

import java.util.List;

public interface ValuesStore {
  List<DatedValue> valuesForSingle(String id);
  List<DatedValue> valuesForCompos(String id);

  void addValuesToSingleLine(String id, List<DatedValue> values);
  boolean removeValueFromSingleLine(String id, String date);
}
