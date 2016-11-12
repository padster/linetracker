package today.useit.linetracker.store.cloud;

import today.useit.linetracker.store.ItemNotFoundException;

import com.google.cloud.datastore.Entity;

/** Perform basic ACL validation. */
public class ACLs {
  public static String userForEntity(Entity entity) {
    Object result = entity.getString("uid");
    return result == null ? null : result.toString();
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
