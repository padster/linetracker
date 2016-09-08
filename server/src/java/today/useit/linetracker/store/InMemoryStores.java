package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStores implements Stores {
  private final ItemStore<SingleLineMeta> singleStore;
  private final ItemStore<ComposLineMeta> composStore;
  private final ItemStore<GraphsLineMeta> graphsStore;

  public InMemoryStores() {
    this.singleStore = new BaseInMemoryItemStore<SingleLineMeta>();
    this.composStore = new BaseInMemoryItemStore<ComposLineMeta>();
    this.graphsStore = new BaseInMemoryItemStore<GraphsLineMeta>();

    SingleLineMeta sline = new SingleLineMeta();
    sline.id = "abcd1234"; sline.name = "single store";
    sline.link = "http://www.example.com/working";
    this.singleStore.createItem(sline);

    ComposLineMeta cline = new ComposLineMeta();
    cline.id = "1a2b3c4d"; cline.name = "compos store"; cline.op = "plus";
    cline.childMetadata.add(new ChildEntry("abcd1234", "hello store"));
    this.composStore.createItem(cline);

    GraphsLineMeta gline = new GraphsLineMeta();
    gline.id = "1234abcd"; gline.name = "graph store";
    gline.childMetadata.add(new ChildEntry("1a2b3c4d", "compos store"));
    gline.childMetadata.add(new ChildEntry("abcd1234", "hello store"));
    this.graphsStore.createItem(gline);
  }

  public ItemStore<SingleLineMeta> singleStore() {
    return this.singleStore;
  }
  public ItemStore<ComposLineMeta> composStore() {
    return this.composStore;
  }
  public ItemStore<GraphsLineMeta> graphsStore() {
    return this.graphsStore;
  }
}
