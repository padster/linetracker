package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.ItemStore;

import com.google.cloud.datastore.Datastore;

import java.util.List;

public class CloudItemStore<T extends HasId> implements ItemStore<T> {
  private final Datastore db;

  public CloudItemStore(Datastore db) {
    this.db = db;
  }

  public List<T> listItems() {
    // TODO
    return null;
  }

  public T getItem(String id) {
    // TODO
    return null;
  }

  public T createItem(T item) {
    // TODO
    return null;
  }

  public boolean deleteItem(String id) {
    // TODO
    return false;
  }
}
