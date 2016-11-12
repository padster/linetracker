package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class CloudItemStoreGraphs extends CloudItemStore<GraphsLineMeta> {
  public CloudItemStoreGraphs(Datastore db) {
    super(db, Keys.GRAPHS_TYPE);
  }

  protected GraphsLineMeta fromEntity(Entity entity) {
    GraphsLineMeta line = new GraphsLineMeta();
    line.id = entity.key().toString(); // HACK
    line.name = entity.getString("name");
    return line;
  }

  protected Entity toEntity(GraphsLineMeta value) {
    Key key = this.idToKey(value.id);
    return Entity.builder(key)
        .set("name", value.name)
        .build();
  }
}
