package today.useit.linetracker.model;

import java.util.List;

public class ChildEntry {
  public final String id;
  public final String name;

  public ChildEntry(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override public int hashCode() {
    return id.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof ChildEntry)
      ? this.id.equals(((ChildEntry)other).id)
      : false;
  }
}
