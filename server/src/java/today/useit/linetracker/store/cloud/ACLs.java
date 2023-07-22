package today.useit.linetracker.store.cloud;

import today.useit.linetracker.store.ItemNotFoundException;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

/** Perform basic ACL validation. */
public class ACLs {
  public static final String USER_PROP = "uid";

  public static String userForEntity(Entity entity) {
    Object result = entity.getString(USER_PROP);
    return result == null ? null : result.toString();
  }

  public static Entity.Builder entityForUser(Key key, String userID) {
    return Entity.newBuilder(key).set(USER_PROP, userID);
  }

  public static void assertUserCanView(Entity entity) throws ItemNotFoundException {
    // TODO
    /*
    User user = Util.getUser();
    if (user == null) {
      Log.error("Someone's hacking values");
      throw new EntityNotFoundException(entity.getKey());
    }

    if (!user.getUserId().equals(userForEntity(entity))) {
      Log.error(user.getEmail() + " is hacking values :(");
      throw new EntityNotFoundException(entity.getKey());
    }
    */
  }
}
