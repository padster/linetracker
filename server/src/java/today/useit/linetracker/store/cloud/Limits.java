package today.useit.linetracker.store.cloud;

import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

public final class Limits {
  // Max number of lines of a given type for a user.
  public static final int LINE_LIMIT_SINGLE_FETCH = 500;
}
