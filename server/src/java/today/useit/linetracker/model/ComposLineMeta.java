package today.useit.linetracker.model;

import java.util.ArrayList;
import java.util.List;

public class ComposLineMeta {
  public String id;
  public String name;
  public List<ChildEntry> childMetadata = new ArrayList<ChildEntry>();
}