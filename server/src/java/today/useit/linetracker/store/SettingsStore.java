package today.useit.linetracker.store;

import today.useit.linetracker.model.Settings;

public interface SettingsStore {
  Settings getSettings();
  void updateSettings(Settings settings);
}
