package today.useit.linetracker.model;

public class SingleLineMeta implements HasId {
  public String id;
  public String name;
  public String link;

  public String id() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override public int hashCode() {
    return id.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof SingleLineMeta)
      ? this.id.equals(((SingleLineMeta)other).id)
      : false;
  }
}
