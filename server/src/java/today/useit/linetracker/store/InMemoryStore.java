package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryStore implements Store {
  private List<SingleLineMeta> singleLines = new ArrayList<>();
  private List<ComposLineMeta> composLines = new ArrayList<>();
  private List<GraphsLineMeta> graphsLines = new ArrayList<>();

  public InMemoryStore() {
    SingleLineMeta sline = new SingleLineMeta();
    sline.id = "abcd1234"; sline.name = "single store";
    sline.link = "http://www.example.com/working";
    singleLines.add(sline);
    ComposLineMeta cline = new ComposLineMeta();
    cline.id = "1a2b3c4d"; cline.name = "compos store";
    cline.childMetadata.add(new ChildEntry("abcd1234", "hello store"));
    composLines.add(cline);
    GraphsLineMeta gline = new GraphsLineMeta();
    gline.id = "1234abcd"; gline.name = "graph store";
    gline.childMetadata.add(new ChildEntry("1a2b3c4d", "compos store"));
    gline.childMetadata.add(new ChildEntry("abcd1234", "hello store"));
    graphsLines.add(gline);
  }

  // LIST

  public List<SingleLineMeta> listSingleMeta() {
    return this.singleLines;
  }
  public List<ComposLineMeta> listComposMeta() {
    return this.composLines;
  }
  public List<GraphsLineMeta> listGraphsMeta() {
    return this.graphsLines;
  }

  // GET

  public SingleLineMeta getSingleMeta(String id) {
    return this.singleLines.stream()
      .filter(line -> line.id.equals(id))
      .findFirst().orElse(null);
  }
  public ComposLineMeta getComposMeta(String id) {
    return this.composLines.stream()
      .filter(line -> line.id.equals(id))
      .findFirst().orElse(null);
  }
  public GraphsLineMeta getGraphsMeta(String id) {
    return this.graphsLines.stream()
      .filter(line -> line.id.equals(id))
      .findFirst().orElse(null);
  }

  // CREATE

  public SingleLineMeta createSingleMeta(SingleLineMeta line) {
    // TODO - generate ID.
    this.singleLines.add(line);
    return line;
  }
  public ComposLineMeta createComposMeta(ComposLineMeta line) {
    // TODO - generate ID.
    this.composLines.add(line);
    return line;
  }
  public GraphsLineMeta createGraphsMeta(GraphsLineMeta line) {
    // TODO - generate ID.
    this.graphsLines.add(line);
    return line;
  }

  // DELETE

  public boolean deleteSingleMeta(String id) {
    System.out.println("DELETING single: " + id);
    SingleLineMeta meta = this.getSingleMeta(id);
    System.out.println(meta);
    System.out.println(this.singleLines.size());
    boolean result = meta == null ? false : this.singleLines.remove(meta);
    System.out.println(this.singleLines.size());
    return result;
  }
  public boolean deleteComposMeta(String id) {
    ComposLineMeta meta = this.getComposMeta(id);
    return meta == null ? false : this.composLines.remove(meta);
  }
  public boolean deleteGraphsMeta(String id) {
    GraphsLineMeta meta = this.getGraphsMeta(id);
    return meta == null ? false : this.graphsLines.remove(meta);
  }
}
