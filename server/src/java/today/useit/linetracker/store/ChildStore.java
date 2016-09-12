package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

import java.util.List;
import java.util.Map;

// TODO - add class that represents a parsed full ID.
public interface ChildStore {
  Map<String, List<ChildEntry>> allChildrenForType(String type);
  List<ChildEntry> getChildren(String fullID);

  void addChildren(String fullID, List<ChildEntry> children);
  boolean removeChild(String fullID, ChildEntry child);
}
