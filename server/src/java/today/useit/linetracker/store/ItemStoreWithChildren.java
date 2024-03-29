package today.useit.linetracker.store;

import today.useit.linetracker.model.ChildEntry;
import today.useit.linetracker.model.HasChildren;
import today.useit.linetracker.model.HasId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemStoreWithChildren<T extends HasChildren & HasId> implements ItemStore<T> {
  private final ItemStore<T> base;
  private final ChildStore childStore;
  private final String type;

  public ItemStoreWithChildren(ItemStore<T> base, ChildStore childStore, String type) {
    this.base = base;
    this.childStore = childStore;
    this.type = type;
  }

  public List<T> listItems() {
    List<T> items = this.base.listItems();
    final List<String> allIDs = items.stream().map(item -> item.id()).collect(Collectors.toList());
    final Map<String, List<ChildEntry>> allChildren =
      childStore.allChildrenForTypeAndIDs(type, allIDs);
    items.forEach(item -> {
      List<ChildEntry> children = allChildren.get(item.id());
      item.setChildren(children != null ? children : new ArrayList<>());
    });
    return items;
  }

  public T getItem(String id) {
    T item = this.base.getItem(id);
    if (item == null) {
      return null;
    }
    String itemFullID = this.type + "/" + item.id();
    List<ChildEntry> children = this.childStore.getChildren(itemFullID);
    item.setChildren(children);
    return item;
  }

  public T createItem(T item) {
    T newItem = this.base.createItem(item);
    String itemFullID = this.type + "/" + newItem.id();
    this.childStore.addChildren(itemFullID, item.children());
    return newItem;
  }

  public boolean deleteItem(String id) {
    String itemFullID = this.type + "/" + id;
    this.childStore.removeAllChildren(itemFullID);
    return this.base.deleteItem(id);
  }
}
