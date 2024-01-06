package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

/** Holder of all stores used within the app. */
public interface Stores {
  /** @return The store for single line metadata. */
  ItemStore<SingleLineMeta> singleStore();

  /** @return The store for composite line metadata. */
  ItemStore<ComposLineMeta> composStore();

  /** @return The store for graph metadata. */
  ItemStore<GraphsLineMeta> graphsStore();

  /** @return Store exposing dated values for single/compos lines. */
  ValuesStore valuesStore();

  /** @return Store mapping parent (compos/graphs) to child (graphs/single). */
  ChildStore childStore();

  /** @return Store holding all per-user settings. */
  SettingsStore settingsStore();
}
