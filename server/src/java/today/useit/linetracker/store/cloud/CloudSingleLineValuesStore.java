package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.SingleLineValuesStore;

import com.google.cloud.datastore.Datastore;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.inject.Provider;

public class CloudSingleLineValuesStore implements SingleLineValuesStore {
  private final Datastore db;
  private final Provider<String> userProvider;

  public CloudSingleLineValuesStore(Datastore db, Provider<String> userProvider) {
    this.db = db;
    this.userProvider = userProvider;
  }

  public SortedSet<DatedValue> values(String id) {
    return new TreeSet<DatedValue>();
  }

  public void addValues(String id, List<DatedValue> values) {
    // TODO
  }

  public boolean removeValue(String id, String date) {
    // TODO
    return false;
  }
}
