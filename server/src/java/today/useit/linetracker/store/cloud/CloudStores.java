package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.*;

import com.google.cloud.datastore.Datastore;

import javax.inject.Inject;

public class CloudStores implements Stores {
  private final ItemStore<SingleLineMeta> singleStore;
  private final ItemStore<ComposLineMeta> composStore;
  private final ItemStore<GraphsLineMeta> graphsStore;
  private final CalculatingValuesStore    valuesStore;
  private final ChildStore                 childStore;
  private final SettingsStore           settingsStore;

  @Inject public CloudStores(Datastore db) {
    this.childStore = new CloudChildStore(db);
    this.singleStore = new CloudItemStore<SingleLineMeta>(db);
    this.composStore = new ItemStoreWithChildren<ComposLineMeta>(
      new CloudItemStore<ComposLineMeta>(db), childStore, "compos");
    this.graphsStore = new ItemStoreWithChildren<GraphsLineMeta>(
      new CloudItemStore<GraphsLineMeta>(db), childStore, "graphs");
    this.valuesStore = new CalculatingValuesStore(
      new CloudSingleLineValuesStore(db), composStore, childStore);
    this.settingsStore = new CloudSettingsStore(db);
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
