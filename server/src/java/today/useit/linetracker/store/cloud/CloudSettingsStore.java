package today.useit.linetracker.store.cloud;

import today.useit.linetracker.model.*;
import today.useit.linetracker.store.SettingsStore;

import com.google.cloud.datastore.Datastore;

public class CloudSettingsStore implements SettingsStore {
  private final Datastore db;

  public CloudSettingsStore(Datastore db) {
    this.db = db;
  }

  public Settings getSettings() {
    // TODO
    return null;
  }

  public void updateSettings(Settings settings) {
    // TODO
  }
}
