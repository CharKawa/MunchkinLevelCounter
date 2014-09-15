package com.g_art.munchkinlevelcounter.settings;

import android.content.SharedPreferences;

/**
 * Created by G_Art on 12/9/2014.
 */
public class SettingsHandler {
    private static SettingsHandler settingsInstance;
    public static final String STATS_SETTINGS = "stats_settings";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    public static SettingsHandler getInstance(SharedPreferences mPrefs) {
        if (settingsInstance == null) {
            settingsInstance = new SettingsHandler(mPrefs);
        }
        return settingsInstance;
    }

    private SettingsHandler(SharedPreferences mPrefs) {
        this.mPrefs = mPrefs;
    }


    public boolean loadSettings() {
        if (!mPrefs.contains(STATS_SETTINGS)) {
            saveDefaultSettings();
        }
        boolean result = mPrefs.getBoolean(STATS_SETTINGS, true);
        return result;
    }

    public boolean saveSettings(boolean checked) {
        prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(STATS_SETTINGS, checked);
        boolean result = prefsEditor.commit();
        return result;
    }

    public boolean saveDefaultSettings() {
        return saveSettings(true);
    }
}
