package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.SingleLineValuesStore;

import java.util.List;
import java.util.SortedSet;

public class CloudSingleLineValuesStore implements SingleLineValuesStore {
  public SortedSet<DatedValue> values(String id) {
    // TODO
    return null;
  }

  public void addValues(String id, List<DatedValue> values) {
    // TODO
  }

  public boolean removeValue(String id, String date) {
    // TODO
    return false;
  }
}
