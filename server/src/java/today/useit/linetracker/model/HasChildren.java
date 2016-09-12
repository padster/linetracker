package today.useit.linetracker.model;

import java.util.List;

public interface HasChildren {
  List<ChildEntry> children();
  void setChildren(List<ChildEntry> children);
}
