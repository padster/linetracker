package today.useit.linetracker.store.cloud;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import java.util.Random;

public final class Keys {
  public static final String SINGLE_TYPE = "LS";
  public static final String COMPOS_TYPE = "LC";
  public static final String GRAPHS_TYPE = "LG";
  public static final String SETTINGS_TYPE = "ST";

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
    return db.newKeyFactory().kind(dbKind).newKey(id);
  }

  // HACK - move elsewhere?
  public static PropertyFilter currentUserFilter() {
    return PropertyFilter.eq("uid", "HACK");
  }
}
