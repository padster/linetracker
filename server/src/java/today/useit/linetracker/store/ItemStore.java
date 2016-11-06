package today.useit.linetracker.store;

import java.util.List;

/** Common interface for storage of all objects of one given type. */
public interface ItemStore<T> {
  /** @return List of all objects of this type (no specific ordering). */
  List<T> listItems();

  /** @return Single item keyed by this ID, or null if none known. */
  T getItem(String id);

  /** Create a new item given values, and return it. */
  T createItem(T item);

  /** Delete an item by id key. */
  boolean deleteItem(String id);
}
