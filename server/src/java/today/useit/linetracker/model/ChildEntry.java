package today.useit.linetracker.model;

import java.util.List;

public class ChildEntry {
  // NOTE: shortened version, 'single'/'compos'/'const'. TODO: switch to enum.
  public final String type;
  public final String id;
  public final Double value;

  // TODO: Switch these two around?
  public ChildEntry(String type, String id) {
    this.type = type;
    this.id = id;
    this.value = null;
  }

  public ChildEntry(String type, double value) {
    this.type = type;
    this.id = null;
    this.value = value;
  }

  @Override public int hashCode() {
    return type.hashCode() * 37 + (value != null ? value.hashCode() : id.hashCode());
  }

  @Override public boolean equals(Object other) {
    if (!(other instanceof ChildEntry)) {
      return false;
    }
    ChildEntry o = (ChildEntry) other;
    if (!this.type.equals(o.type)) {
      return false;
    } else if (this.id == null) {
      return o.id == null && this.value == o.value;
    } else {
      return o.id != null && this.id.equals(o.id);
    }
  }
}
