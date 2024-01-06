package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.SettingsStore;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Transaction;

import jakarta.inject.Provider;

public class CloudSettingsStore implements SettingsStore {
  private final Datastore db;
  private final Provider<String> userProvider;
  private final KeyFactory keyFactory;

  public CloudSettingsStore(Datastore db, Provider<String> userProvider) {
    this.db = db;
    this.userProvider = userProvider;
    this.keyFactory = db.newKeyFactory().setKind(Keys.SETTINGS_TYPE);
  }

  /** @return Settings for the current user. */
  public Settings getSettings() {
    Entity asEntity = db.get(keyFactory.newKey(userProvider.get()));
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
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  // Entity -> Settings translator.
  private Settings entityToSettings(Entity entity) {
    if (entity == null) {
      // Nothing stored yet, so no home ID.
      return new Settings(null);
    }
    return new Settings(entity.getString("homeID"));
  }

  // Settings -> Entity translator.
  private Entity settingsToEntity(Settings settings) {
    Key key = keyFactory.newKey(userProvider.get());
    return Entity.newBuilder(key)
      .set("homeID", settings.homeID)
      .build();
  }
}
