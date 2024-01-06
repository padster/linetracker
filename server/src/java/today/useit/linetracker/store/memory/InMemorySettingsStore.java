package today.useit.linetracker.store.memory;

import jakarta.inject.Provider;
import today.useit.linetracker.model.Settings;
import today.useit.linetracker.store.SettingsStore;

public class InMemorySettingsStore implements SettingsStore {
  private Settings settings = new Settings("");

  private final Provider<String> currentUser;

  public InMemorySettingsStore(Provider<String> currentUser) {
    this.currentUser = currentUser;
  }

  public Settings getSettings() {
    System.out.println("Getting settings for: " + this.currentUser.get());
    return this.settings;
  }

  public void updateSettings(Settings settings) {
    this.settings = settings;
  }
}
