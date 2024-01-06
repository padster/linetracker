package today.useit.linetracker.store.cloud;

import com.github.padster.guiceserver.Annotations.CurrentUser;
import today.useit.linetracker.model.*;
import today.useit.linetracker.store.*;

import com.google.cloud.datastore.Datastore;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

public class CloudStores implements Stores {
  private final ItemStore<SingleLineMeta> singleStore;
  private final ItemStore<ComposLineMeta> composStore;
  private final ItemStore<GraphsLineMeta> graphsStore;
  private final CalculatingValuesStore    valuesStore;
  private final ChildStore                 childStore;
  private final SettingsStore           settingsStore;

  @Inject public CloudStores(Datastore db, @CurrentUser Provider<String> userProvider) {
    this.childStore = new CloudChildStore(db);
    this.singleStore = new CloudItemStoreSingle(db, userProvider);
    this.composStore = new ItemStoreWithChildren<ComposLineMeta>(
      new CloudItemStoreCompos(db, userProvider), childStore, "compos");
    this.graphsStore = new ItemStoreWithChildren<GraphsLineMeta>(
      new CloudItemStoreGraphs(db, userProvider), childStore, "graphs");
    this.valuesStore = new CalculatingValuesStore(
      new CloudSingleLineValuesStore(db, userProvider), composStore, childStore);
    this.settingsStore = new CloudSettingsStore(db, userProvider);
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
