package today.useit.linetracker.store;

import today.useit.linetracker.model.*;

import java.util.List;

public interface Store {
  List<SingleLineMeta> listSingleMeta();
  List<ComposLineMeta> listComposMeta();
  List<GraphsLineMeta> listGraphsMeta();

  SingleLineMeta getSingleMeta(String id);
  ComposLineMeta getComposMeta(String id);
  GraphsLineMeta getGraphsMeta(String id);

  SingleLineMeta createSingleMeta(SingleLineMeta line);
  ComposLineMeta createComposMeta(ComposLineMeta line);
  GraphsLineMeta createGraphsMeta(GraphsLineMeta line);

  boolean deleteSingleMeta(SingleLineMeta line);
  boolean deleteComposMeta(ComposLineMeta line);
  boolean deleteGraphsMeta(GraphsLineMeta line);
}
