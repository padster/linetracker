package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import jakarta.inject.Provider;

public class CloudItemStoreCompos extends CloudItemStore<ComposLineMeta> {
  public CloudItemStoreCompos(Datastore db, Provider<String> userProvider) {
    super(db, Keys.COMPOS_TYPE, userProvider);
  }

  protected ComposLineMeta fromEntity(Entity entity) {
    ComposLineMeta line = new ComposLineMeta();
    line.id = entity.getKey().getName(); // HACK
    line.name = entity.getString("name");
    line.op = entity.getString("op");
    return line;
  }

  protected Entity toEntity(ComposLineMeta value) {
    Entity.Builder builder = entityForID(value.id);
    return builder
        .set("name", value.name)
        .set("op", value.op)
        .build();
  }
}
