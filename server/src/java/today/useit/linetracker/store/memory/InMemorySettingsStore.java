package today.useit.linetracker.store.memory;

import today.useit.linetracker.model.Settings;
import today.useit.linetracker.store.SettingsStore;

public class InMemorySettingsStore implements SettingsStore {
  private Settings settings = new Settings("");

  public Settings getSettings() {
    return this.settings;
  }

  public void updateSettings(Settings settings) {
    this.settings = settings;
  }
}
