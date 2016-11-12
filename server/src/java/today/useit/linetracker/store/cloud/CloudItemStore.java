package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.ItemStore;
import today.useit.linetracker.store.ItemNotFoundException;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class CloudItemStore<T extends HasId> implements ItemStore<T> {
  protected final Datastore db;
  protected final String dbKind;

  public CloudItemStore(Datastore db, String dbKind) {
    this.db = db;
    this.dbKind = dbKind;
  }

  public List<T> listItems() {
    Query<Entity> query = Query.entityQueryBuilder()
      .kind(this.dbKind)
      .filter(Keys.currentUserFilter())
      .orderBy(OrderBy.asc("name"))
      .limit(Limits.LINE_LIMIT_SINGLE_FETCH)
      .build();
    QueryResults<Entity> result = db.run(query);
    final Iterable<Entity> resultIterable = () -> result;
    Stream<Entity> resultStream = StreamSupport.stream(resultIterable.spliterator(), false);
    // Stream<Entity> resultStream = Stream.generate(result::next);
    return resultStream.map(entity -> {
      System.out.println("HERE");
      return this.fromEntity(entity);
    }).collect(Collectors.toList());
  }

  public T getItem(String id) {
    try {
      Entity entity = db.get(idToKey(id));
      ACLs.assertUserCanView(entity);
      return fromEntity(entity);
    } catch (ItemNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public T createItem(T item) {
    // TODO
    return null;
  }

  public boolean deleteItem(String id) {
    // TODO
    return false;
  }

  protected Key idToKey(String id) {
    return Keys.forLine(db, dbKind, id);
  }

  protected abstract T fromEntity(Entity entity);
  protected abstract Entity toEntity(T value);
}
