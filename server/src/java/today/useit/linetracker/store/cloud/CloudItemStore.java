package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.ItemStore;

import java.util.List;

public class CloudItemStore<T extends HasId> implements ItemStore<T> {
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
