package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

import java.util.List;
import java.util.Map;

// TODO - add class that represents a parsed full ID.
/** Store mapping parent graph/compos lines to their set of children. */
public interface ChildStore {
  /** Return all child mappings for a given list of ids. */
  Map<String, List<ChildEntry>> allChildrenForTypeAndIDs(String type, List<String> ids);

  /** Return all children for a given (full) line ID, empty if no children. */
  List<ChildEntry> getChildren(String fullID);

  /** Adds a collection of children to a given line. */
  void addChildren(String fullID, List<ChildEntry> children);

  /** Removes a single child from a line. */
  boolean removeChild(String fullID, ChildEntry child);

  /** Removes all children from a line. */
  boolean removeAllChildren(String fullID);
}
