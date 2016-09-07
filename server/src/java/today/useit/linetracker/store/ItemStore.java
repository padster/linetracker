package today.useit.linetracker.store;

import java.util.List;

public interface ItemStore<T> {
  List<T> listItems();
  T getItem(String id);
  T createItem(T item);
  boolean deleteItem(String id);
}
