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
import javax.inject.Provider;

public abstract class CloudItemStore<T extends HasId> implements ItemStore<T> {
  protected final Datastore db;
  protected final String dbKind;
  protected final Provider<String> userProvider;

  public CloudItemStore(Datastore db, String dbKind, Provider<String> userProvider) {
    this.db = db;
    this.dbKind = dbKind;
    this.userProvider = userProvider;
  }

  public List<T> listItems() {
    System.out.println("LISTING");
    Query<Entity> query = Query.entityQueryBuilder()
      .kind(this.dbKind)
      .filter(Keys.currentUserFilter())
      .orderBy(OrderBy.asc("name"))
      .limit(Limits.LINE_LIMIT_SINGLE_FETCH)
      .build();
    QueryResults<Entity> result = db.run(query);
    final Iterable<Entity> resultIterable = () -> result;
    Stream<Entity> resultStream = StreamSupport.stream(resultIterable.spliterator(), false);
    return resultStream.map(entity -> {
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
    String newId = Keys.newForLine(this.db, this.dbKind);
    item.setId(newId);
    Entity ent = toEntity(item);
    db.add(ent);
    return item;
  }

  public boolean deleteItem(String id) {
    T item = getItem(id); // User can get
    if (item == null) {
      return false;
    }
    db.delete(Keys.forLine(db, dbKind, id));
    return true;
  }

  protected Key idToKey(String id) {
    return Keys.forLine(db, dbKind, id);
  }

  protected Entity.Builder entityForID(String id) {
    return ACLs.entityForUser(idToKey(id), userProvider.get());
  }

  protected abstract T fromEntity(Entity entity);
  protected abstract Entity toEntity(T value);
}
