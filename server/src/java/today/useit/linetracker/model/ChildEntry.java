package today.useit.linetracker.model;

import java.util.List;

public class ChildEntry {
  // NOTE: shortened version, s = single, c = compos.
  public final String type;
  public final String id;

  public ChildEntry(String type, String id) {
    this.type = type;
    this.id = id;
  }

  @Override public int hashCode() {
    return type.hashCode() * 37 + id.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof ChildEntry)
      ? type.equals(((ChildEntry)other).type) && id.equals(((ChildEntry)other).id)
      : false;
  }
}
