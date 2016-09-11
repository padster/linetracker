package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

import java.util.List;

public interface Stores {
  ItemStore<SingleLineMeta> singleStore();
  ItemStore<ComposLineMeta> composStore();
  ItemStore<GraphsLineMeta> graphsStore();
  ValuesStore valuesStore();
}
