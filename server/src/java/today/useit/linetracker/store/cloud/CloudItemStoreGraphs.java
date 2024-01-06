package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import jakarta.inject.Provider;

public class CloudItemStoreGraphs extends CloudItemStore<GraphsLineMeta> {
  public CloudItemStoreGraphs(Datastore db, Provider<String> userProvider) {
    super(db, Keys.GRAPHS_TYPE, userProvider);
  }

  protected GraphsLineMeta fromEntity(Entity entity) {
    GraphsLineMeta line = new GraphsLineMeta();
    line.id = entity.getKey().getName(); // HACK
    line.name = entity.getString("name");
    return line;
  }

  protected Entity toEntity(GraphsLineMeta value) {
    Entity.Builder builder = entityForID(value.id);
    return builder
        .set("name", value.name)
        .build();
  }
}
