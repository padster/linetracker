package today.useit.linetracker.model;

import java.util.ArrayList;
import java.util.List;

public class ComposLineMeta implements HasId {
  public String id;
  public String name;
  public List<ChildEntry> childMetadata = new ArrayList<>();

  public String id() {
    return this.id;
  }

  @Override public int hashCode() {
    return id.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof ComposLineMeta)
      ? this.id.equals(((ComposLineMeta)other).id)
      : false;
  }
}
