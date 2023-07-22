package today.useit.linetracker.model;

import java.util.ArrayList;
import java.util.List;

public class ComposLineMeta implements HasId, HasChildren {
  public String id;
  public String name;
  public String op;
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

  public String op() {
    return this.op;
  }

  public void setOp(String op) {
    this.op = op;
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
    return (other instanceof ComposLineMeta)
      ? this.id.equals(((ComposLineMeta)other).id)
      : false;
  }
}
