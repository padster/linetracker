package today.useit.linetracker.store;

import today.useit.linetracker.model.Settings;

/** Expose user-specific settings metadata. */
public interface SettingsStore {
  /** @return The settings for the current user. */
  Settings getSettings();

  /** Overwrites old settings with new value. */
  void updateSettings(Settings settings);
}
