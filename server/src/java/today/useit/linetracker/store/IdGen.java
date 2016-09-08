package today.useit.linetracker.store;

import java.util.Random;


public final class IdGen {
  private static final Random RANDOM = new Random();
  private static final int ID_LENGTH = 12;
  private static final String ID_CHARS = "0123456789abcdef";

  /** Generates a random ID, no guarantee of collision-free. */
  public static String randomId() {
    StringBuilder id = new StringBuilder();
    for (int i = 0; i < ID_LENGTH; i++) {
      id.append(ID_CHARS.charAt(RANDOM.nextInt(ID_CHARS.length())));
    }
    return id.toString();
  }

  // TODO - re-enable once data storage is used.
  /*
  public static String generateId(String parent) {
    for (int i = 0; i < 20; i++) {
      String id = randomId();
      Key key = Keys.forParent(parent, id);
      if (!taken(key)) {
        return id;
      }
      if (i == 10) {
        Log.warn("IDs running out?");
      }
    }
    Log.error("IDs ran out!");
    return "NOID";
  }
  private static boolean taken(Key key) {
    DatastoreService db = DatastoreServiceFactory.getDatastoreService();
    Query q = new Query().setFilter(
        new Query.FilterPredicate("__key__", Query.FilterOperator.EQUAL, key)).setKeysOnly();
    return db.prepare(q).asIterator().hasNext();
  }
  */
}
