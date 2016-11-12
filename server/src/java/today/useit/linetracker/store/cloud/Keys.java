package today.useit.linetracker.store.cloud;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

public final class Keys {
  public static final String SINGLE_TYPE = "LS";
  public static final String COMPOS_TYPE = "LC";
  public static final String GRAPHS_TYPE = "LG";
  public static final String SETTINGS_TYPE = "ST";

  public static Key forLine(Datastore db, String key, String kind) {
    // TODO - cache key factory.
    return db.newKeyFactory().kind(kind).newKey(key);
  }

  // HACK - move elsewhere?
  public static PropertyFilter currentUserFilter() {
    return PropertyFilter.eq("uid", "HACK");
  }
}
