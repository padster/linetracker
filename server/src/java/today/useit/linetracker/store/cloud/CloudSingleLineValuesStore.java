package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.SingleLineValuesStore;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.inject.Provider;

public class CloudSingleLineValuesStore implements SingleLineValuesStore {
  private final Datastore db;
  private final Provider<String> userProvider;

  public CloudSingleLineValuesStore(Datastore db, Provider<String> userProvider) {
    this.db = db;
    this.userProvider = userProvider;
  }

  public SortedSet<DatedValue> values(String id) {
    verifyUserCanRead(id);
    Query<Entity> query = Keys.datedValueQuery(db, id);
    QueryResults<Entity> result = db.run(query);
    final Iterable<Entity> resultIterable = () -> result;
    Stream<Entity> resultStream = StreamSupport.stream(resultIterable.spliterator(), false);

    final TreeSet<DatedValue> valuesSet = new TreeSet<>();
    resultStream.forEach(entity -> {
      valuesSet.add(new DatedValue(entity.key().name(), entity.getDouble("v")));
    });
    return valuesSet;
  }

  public void addValues(String id, List<DatedValue> values) {
    verifyUserCanRead(id);
    Entity[] entities = values.stream().map(value -> {
      Key key = Keys.forDatedValue(db, id, value.t);
      Entity.Builder builder = ACLs.entityForUser(key, userProvider.get());
      builder.set("v", value.v);
      return builder.build();
    }).toArray(size -> new Entity[size]);
    db.put(entities);
  }

  public boolean removeValue(String id, String date) {
    verifyUserCanRead(id);
    Key key = Keys.forDatedValue(db, id, date);
    db.delete(key);
    return true;
  }

  // TODO - test a user can read this line first...
  private void verifyUserCanRead(String id) {
    // nothing yet...
  }
}
