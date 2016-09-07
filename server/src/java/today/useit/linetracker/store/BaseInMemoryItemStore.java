package today.useit.linetracker.store;

import today.useit.linetracker.model.HasId;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class BaseInMemoryItemStore<T extends HasId> implements ItemStore<T> {
  private final SortedMap<String, T> items = new TreeMap<String, T>();

  public List<T> listItems() {
    return new ArrayList<>(items.values());
  }

  public T getItem(String id) {
    return items.get(id);
  }

  public T createItem(T item) {
    // TODO - generate ID
    items.put(item.id(), item);
    return item;
  }

  public boolean deleteItem(String id) {
    return items.remove(id) != null;
  }
}
