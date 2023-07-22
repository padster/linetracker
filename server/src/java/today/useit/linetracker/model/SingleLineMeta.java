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

  public String name() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String link() {
    return this.link;
  }

  public void setLink(String link) {
    this.link = link;
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
