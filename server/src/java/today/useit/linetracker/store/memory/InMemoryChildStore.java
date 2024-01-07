package today.useit.linetracker.store.memory;

import today.useit.linetracker.store.ChildStore;
import today.useit.linetracker.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryChildStore implements ChildStore {
  // type, id, children
  private final Map<String, Map<String, List<ChildEntry>>> entries = new HashMap<>();

  public Map<String, List<ChildEntry>> allChildrenForTypeAndIDs(String type, List<String> ids) {
    if (!entries.containsKey(type)) {
      entries.put(type, new HashMap<String, List<ChildEntry>>());
    }
    // NOTE: just return everything, don't bother filtering by needed IDs.
    return entries.get(type);
  }

  public List<ChildEntry> getChildren(String fullID) {
    String[] parts = fullID.split("/");
    return this.safeGet(parts[0], parts[1]);
  }

  public void addChildren(String fullID, List<ChildEntry> newChildren) {
    String[] parts = fullID.split("/");
    final List<ChildEntry> children = this.safeGet(parts[0], parts[1]);
    children.addAll(newChildren);
  }

  public boolean removeChild(String fullID, ChildEntry child) {
    String[] parts = fullID.split("/");
    final List<ChildEntry> children = this.safeGet(parts[0], parts[1]);
    return children.remove(child);
  }

  public boolean removeAllChildren(String fullID) {
    String[] parts = fullID.split("/");
    final List<ChildEntry> children = this.safeGet(parts[0], parts[1]);
    children.clear();
    return true;
  }

  private List<ChildEntry> safeGet(String type, String id) {
    if (!entries.containsKey(type)) {
      entries.put(type, new HashMap<String, List<ChildEntry>>());
    }
    Map<String, List<ChildEntry>> forType = entries.get(type);
    if (!forType.containsKey(id)) {
      forType.put(id, new ArrayList<ChildEntry>());
    }
    return forType.get(id);
  }
}
