package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class CloudItemStoreCompos extends CloudItemStore<ComposLineMeta> {
  public CloudItemStoreCompos(Datastore db) {
    super(db, Keys.COMPOS_TYPE);
  }

  protected ComposLineMeta fromEntity(Entity entity) {
    ComposLineMeta line = new ComposLineMeta();
    line.id = entity.key().toString(); // HACK
    line.name = entity.getString("name");
    line.op = entity.getString("op");
    return line;
  }

  protected Entity toEntity(ComposLineMeta value) {
    Key key = this.idToKey(value.id);
    return Entity.builder(key)
        .set("name", value.name)
        .set("op", value.op)
        .build();
  }
}
