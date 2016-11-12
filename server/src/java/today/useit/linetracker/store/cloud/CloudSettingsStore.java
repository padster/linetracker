package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.SettingsStore;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.Transaction;

public class CloudSettingsStore implements SettingsStore {
  private final Datastore db;
  private final String userID;
  private final KeyFactory keyFactory;

  public CloudSettingsStore(Datastore db) {
    this.db = db;
    this.userID = "HACK"; // TODO - inject.
    this.keyFactory = db.newKeyFactory().kind(Keys.SETTINGS_TYPE);
  }

  /** @return Settings for the current user. */
  public Settings getSettings() {
    Entity asEntity = db.get(keyFactory.newKey(userID));
    return entityToSettings(asEntity);
  }

  /** Stores updated settings for the current user. */
  public void updateSettings(Settings settings) {
    Transaction tx = db.newTransaction();
    try {
      Entity asEntity = settingsToEntity(settings);
      tx.put(asEntity);
      tx.commit();
    } finally {
      if (tx.active()) {
        tx.rollback();
      }
    }
  }

  // Entity -> Settings translator.
  private Settings entityToSettings(Entity entity) {
    System.out.println("HERE " + entity);
    if (entity == null) {
      // Nothing stored yet, so no home ID.
      return new Settings(null);
    }
    return new Settings(entity.getString("homeID"));
  }

  // Settings -> Entity translator.
  private Entity settingsToEntity(Settings settings) {
    Key key = keyFactory.newKey(userID);
    return Entity.builder(key)
      .set("homeID", settings.homeID)
      .build();
  }
}
