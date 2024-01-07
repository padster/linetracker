package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.ChildStore;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.HashMap;

public class CloudChildStore implements ChildStore {
  private final Datastore db;

  public CloudChildStore(Datastore db) {
    this.db = db;
  }

  public Map<String, List<ChildEntry>> allChildrenForTypeAndIDs(String type, List<String> ids) {
    HashMap<String, List<ChildEntry>> result = new HashMap<>();
    for (String id : ids) {
      String fullID = type + "/" + id;
      result.put(fullID, this.getChildren(fullID));
    }
    return result;
  }

  public List<ChildEntry> getChildren(String fullID) {
    // fullID = "LS/123456789012"
    // Split into type (LS) & id (123456789012)
    String[] parts = fullID.split("/");
    Query<Entity> query = Keys.childEntryQuery(db, parts[0], parts[1]);

    QueryResults<Entity> result = db.run(query);
    final Iterable<Entity> resultIterable = () -> result;
    Stream<Entity> resultStream = StreamSupport.stream(resultIterable.spliterator(), false);
    return resultStream.map(entity -> {
      return this.fromEntity(entity);
    }).collect(Collectors.toList());
  }

  public void addChildren(String fullID, List<ChildEntry> children) {
    System.out.println("Adding " + children.size() + " children to " + fullID);
    String[] parts = fullID.split("/");
    for (ChildEntry child : children) {
      db.put(toEntity(parts[0], parts[1], child));
    }
  }

  public boolean removeChild(String fullID, ChildEntry child) {
    String[] parts = fullID.split("/");
    db.delete(Keys.forChildEntry(db, parts[0], parts[1], child));
    return true;
  }

  public boolean removeAllChildren(String fullID) {
    String[] parts = fullID.split("/");
    Query<Entity> query = Keys.childEntryQuery(db, parts[0], parts[1]);
    QueryResults<Entity> result = db.run(query);
    System.out.println("Removing all child links for " + fullID + "...");
    final Iterable<Entity> resultIterable = () -> result;
    Stream<Entity> resultStream = StreamSupport.stream(resultIterable.spliterator(), false);
    resultStream.forEach(entity -> {
      db.delete(entity.getKey());
    });
    return true;
  }

  private ChildEntry fromEntity(Entity entity) {
    String type = entity.getString("type");
    if (entity.contains("value")) {
      return new ChildEntry(type, entity.getDouble("value"));
    } else {
      return new ChildEntry(type, entity.getString("name"));
    }
  } 

  private Entity toEntity(String parentType, String parentId, ChildEntry entry) {
    // NOTE: no userID added:
    Key key = Keys.forChildEntry(db, parentType, parentId, entry);
    Entity.Builder builder = Entity.newBuilder(key);
    builder.set("type", entry.type);
    if (entry.value != null) {
      builder.set("value", entry.value);
    } else {
      builder.set("name", entry.id);
    }
    return builder.build();
  }
}
