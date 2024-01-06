package today.useit.linetracker.store.cloud;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import java.util.Random;

public final class Keys {
  public static final String SINGLE_TYPE   = "LS";
  public static final String COMPOS_TYPE   = "LC";
  public static final String GRAPHS_TYPE   = "LG";
  public static final String SETTINGS_TYPE = "ST";
  public static final String VALUE_TYPE    = "DV";

  private static final Random RANDOM = new Random();
  private static final int ID_LENGTH = 12;
  private static final String ID_CHARS = "0123456789abcdef";

  /** Generates a random ID, no guarantee of collision-free. */
  private static String randomId() {
    StringBuilder id = new StringBuilder();
    for (int i = 0; i < ID_LENGTH; i++) {
      id.append(ID_CHARS.charAt(RANDOM.nextInt(ID_CHARS.length())));
    }
    return id.toString();
  }

  public static String newForLine(Datastore db, String dbKind) {
    // TODO
    return randomId();
  }

  public static Key forLine(Datastore db, String dbKind, String id) {
    // TODO - cache key factory.
    return db.newKeyFactory()
      .setKind(dbKind)
      .newKey(id);
  }

  public static Key forDatedValue(Datastore db, String lineId, String yyyymmdd) {
    // Values can only be on single lines.
    return db.newKeyFactory()
      .addAncestors(PathElement.of(SINGLE_TYPE, lineId))
      .setKind(VALUE_TYPE)
      .newKey(yyyymmdd);
  }

  public static Query<Entity> datedValueQuery(Datastore db, String lineId) {
    Key lineKey = db.newKeyFactory().setKind(SINGLE_TYPE).newKey(lineId);
    return Query.newEntityQueryBuilder()
      .setKind(VALUE_TYPE)
      .setFilter(PropertyFilter.hasAncestor(lineKey))
      .build();
  }

  // HACK - move elsewhere?
  public static PropertyFilter currentUserFilter(String userId) {
    return PropertyFilter.eq("uid", userId);
  }
}
