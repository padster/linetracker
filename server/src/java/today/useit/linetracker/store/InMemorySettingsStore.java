package today.useit.linetracker.store;

import today.useit.linetracker.model.Settings;

public class InMemorySettingsStore implements SettingsStore {
  private Settings settings = new Settings("");

  public Settings getSettings() {
    return this.settings;
  }

  public void updateSettings(Settings settings) {
    this.settings = settings;
  }
}
