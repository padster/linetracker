package today.useit.linetracker.model;

import java.util.ArrayList;
import java.util.List;

public class GraphsLineMeta {
  public String id;
  public String name;
  public List<ChildEntry> childMetadata = new ArrayList<>();

  @Override public int hashCode() {
    return id.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof GraphsLineMeta)
      ? this.id.equals(((GraphsLineMeta)other).id)
      : false;
  }
}
