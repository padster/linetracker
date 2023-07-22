package today.useit.linetracker.model;

import java.util.ArrayList;
import java.util.List;

public class GraphsLineMeta implements HasId, HasChildren {
  public String id;
  public String name;
  public List<ChildEntry> childMetadata = new ArrayList<>();

  public String id() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String name() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ChildEntry> children() {
    return this.childMetadata;
  }

  public void setChildren(List<ChildEntry> children) {
    this.childMetadata = children;
  }

  @Override public int hashCode() {
    return id.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof GraphsLineMeta)
      ? this.id.equals(((GraphsLineMeta)other).id)
      : false;
  }
}
