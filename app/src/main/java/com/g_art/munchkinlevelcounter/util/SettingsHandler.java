package com.g_art.munchkinlevelcounter.util;

import android.content.SharedPreferences;

/**
 * Created by G_Art on 12/9/2014.
 */
public class SettingsHandler {
    public static final String MAX_LVL_SETTINGS = "max_lvl_settings";
    public static final int DEFAULT_MAX_LVL = 10;
    public static final int MIN_LVL = 1;
    private static SettingsHandler settingsInstance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private int maxLvl = DEFAULT_MAX_LVL;

    private SettingsHandler(SharedPreferences mPrefs) {
        this.mPrefs = mPrefs;
    }

    public static SettingsHandler getInstance(SharedPreferences mPrefs) {
        if (settingsInstance == null) {
            settingsInstance = new SettingsHandler(mPrefs);
        } else {
            settingsInstance.setSharedPreferences(mPrefs);
        }
        return settingsInstance;
    }

    public boolean loadSettings() {
        if (!mPrefs.contains(MAX_LVL_SETTINGS)) {
            saveDefaultSettings();
        }

        maxLvl = mPrefs.getInt(MAX_LVL_SETTINGS, DEFAULT_MAX_LVL);
        return true;
    }

    public boolean saveSettings(int maxLvl) {
        prefsEditor = mPrefs.edit();
        prefsEditor.putInt(MAX_LVL_SETTINGS, maxLvl);
        boolean result;
        try {
            prefsEditor.apply();
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean saveDefaultSettings() {
        return saveSettings(DEFAULT_MAX_LVL);
    }

    public int getMaxLvl() {
        loadSettings();
        return maxLvl;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.mPrefs = sharedPreferences;
    }
}
