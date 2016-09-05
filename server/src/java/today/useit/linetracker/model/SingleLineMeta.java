package today.useit.linetracker.model;

public class SingleLineMeta {
  public String id;
  public String name;
  public String link;

  @Override public int hashCode() {
    return id.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof SingleLineMeta)
      ? this.id.equals(((SingleLineMeta)other).id)
      : false;
  }
}
