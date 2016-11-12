package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class CloudItemStoreSingle extends CloudItemStore<SingleLineMeta> {
  public CloudItemStoreSingle(Datastore db) {
    super(db, Keys.SINGLE_TYPE);
  }

  protected SingleLineMeta fromEntity(Entity entity) {
    SingleLineMeta line = new SingleLineMeta();
    line.id = entity.key().toString(); // HACK
    line.name = entity.getString("name");
    line.link = entity.getString("link");
    return line;
  }

  protected Entity toEntity(SingleLineMeta value) {
    Key key = this.idToKey(value.id);
    return Entity.builder(key)
        .set("name", value.name)
        .set("link", value.link)
        .build();
  }
}
