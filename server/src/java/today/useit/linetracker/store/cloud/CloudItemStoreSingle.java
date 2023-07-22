package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

import javax.inject.Provider;

public class CloudItemStoreSingle extends CloudItemStore<SingleLineMeta> {
  public CloudItemStoreSingle(Datastore db, Provider<String> userProvider) {
    super(db, Keys.SINGLE_TYPE, userProvider);
  }

  protected SingleLineMeta fromEntity(Entity entity) {
    SingleLineMeta line = new SingleLineMeta();
    line.id = entity.getKey().getName();
    line.name = entity.getString("name");
    if (entity.contains("link")) {
      line.link = entity.getString("link");
    }
    return line;
  }

  protected Entity toEntity(SingleLineMeta value) {
    Entity.Builder builder = entityForID(value.id);
    builder.set("name", value.name);
    if (value.link != null) {
      builder.set("link", value.link);
    }
    return builder.build();
  }
}
