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
  private final ChildStore                 childStore;
  private final SettingsStore           settingsStore;

  public InMemoryStores() {
    this.childStore = new InMemoryChildStore();
    this.singleStore = new InMemoryItemStore<SingleLineMeta>();
    this.composStore = new ItemStoreWithChildren<ComposLineMeta>(
      new InMemoryItemStore<ComposLineMeta>(), childStore, "compos");
    this.graphsStore = new ItemStoreWithChildren<GraphsLineMeta>(
      new InMemoryItemStore<GraphsLineMeta>(), childStore, "graphs");
    this.valuesStore = new CalculatingValuesStore(
      new InMemorySingleLineValuesStore(), composStore, childStore);
    this.settingsStore = new InMemorySettingsStore();

    SingleLineMeta sline = new SingleLineMeta();
    sline.name = "single store";
    sline.link = "http://www.example.com/working";
    sline = this.singleStore.createItem(sline);

    this.valuesStore.addValuesToSingleLine(sline.id, Arrays.asList(
      new DatedValue("20160901",  5.0),
      new DatedValue("20160902", 10.0),
      new DatedValue("20160904",  8.0)
    ));

    ComposLineMeta cline = new ComposLineMeta();
    cline.name = "compos store"; cline.op = "plus";
    cline = this.composStore.createItem(cline);
    this.childStore.addChildren("compos/" + cline.id, Arrays.asList(
      new ChildEntry("single", sline.id),
      new ChildEntry("single", sline.id)
    ));

    GraphsLineMeta gline = new GraphsLineMeta();
    gline.name = "graph store";
    gline = this.graphsStore.createItem(gline);
    this.childStore.addChildren("graphs/" + gline.id, Arrays.asList(
      new ChildEntry("compos", cline.id),
      new ChildEntry("single", sline.id)
    ));

    GraphsLineMeta gline2 = new GraphsLineMeta();
    gline2.name = "home graph";
    gline2 = this.graphsStore.createItem(gline2);
    this.childStore.addChildren("graphs/" + gline2.id, Arrays.asList(
      new ChildEntry("single", sline.id)
    ));

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
  public ChildStore childStore() {
    return this.childStore;
  }
  public SettingsStore settingsStore() {
    return this.settingsStore;
  }
}
