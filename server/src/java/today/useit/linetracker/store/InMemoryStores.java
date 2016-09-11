package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryStores implements Stores {
  private final ItemStore<SingleLineMeta> singleStore;
  private final ItemStore<ComposLineMeta> composStore;
  private final ItemStore<GraphsLineMeta> graphsStore;
  private final CalculatingValuesStore    valuesStore;

  public InMemoryStores() {
    this.singleStore = new InMemoryItemStore<SingleLineMeta>();
    this.composStore = new InMemoryItemStore<ComposLineMeta>();
    this.graphsStore = new InMemoryItemStore<GraphsLineMeta>();
    this.valuesStore = new CalculatingValuesStore(new InMemorySingleLineValuesStore(), composStore);

    SingleLineMeta sline = new SingleLineMeta();
    sline.id = "abcd1234"; sline.name = "single store";
    sline.link = "http://www.example.com/working";
    sline = this.singleStore.createItem(sline);

    this.valuesStore.addValuesToSingleLine(sline.id, Arrays.asList(
      new DatedValue("20160901",  5.0),
      new DatedValue("20160902", 10.0),
      new DatedValue("20160904",  8.0)
    ));

    ComposLineMeta cline = new ComposLineMeta();
    cline.id = "1a2b3c4d"; cline.name = "compos store"; cline.op = "plus";
    cline.childMetadata.add(new ChildEntry("abcd1234", "hello store"));
    cline = this.composStore.createItem(cline);

    GraphsLineMeta gline = new GraphsLineMeta();
    gline.id = "1234abcd"; gline.name = "graph store";
    gline.childMetadata.add(new ChildEntry("1a2b3c4d", "compos store"));
    gline.childMetadata.add(new ChildEntry("abcd1234", "hello store"));
    gline = this.graphsStore.createItem(gline);
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
  public ValuesStore valuesStore() {
    return this.valuesStore;
  }
}
