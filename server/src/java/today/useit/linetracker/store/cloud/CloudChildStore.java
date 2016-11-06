package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.ChildStore;

import java.util.List;
import java.util.Map;

public class CloudChildStore implements ChildStore {
  public Map<String, List<ChildEntry>> allChildrenForType(String type) {
    // TODO
    return null;
  }

  public List<ChildEntry> getChildren(String fullID) {
    // TODO
    return null;
  }

  public void addChildren(String fullID, List<ChildEntry> children) {
    // TODO
  }

  public boolean removeChild(String fullID, ChildEntry child) {
    // TODO
    return false;
  }
}
