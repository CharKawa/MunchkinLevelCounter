package com.g_art.munchkinlevelcounter.util;

import android.content.SharedPreferences;

/**
 * Created by G_Art on 12/9/2014.
 */
public class SettingsHandler {
    private static SettingsHandler settingsInstance;

    public static final String STATS_SETTINGS = "stats_settings";
    public static final String MAX_LVL_SETTINGS = "max_lvl_settings";
    public static final int DEFAULT_MAX_LVL = 10;
    public static final int MIN_LVL = 1;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private boolean isStats = false;
    private int maxLvl = DEFAULT_MAX_LVL;

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
        if (!mPrefs.contains(STATS_SETTINGS) || !mPrefs.contains(MAX_LVL_SETTINGS)) {
            saveDefaultSettings();
        }

        isStats = mPrefs.getBoolean(STATS_SETTINGS, true);
        maxLvl = mPrefs.getInt(MAX_LVL_SETTINGS, DEFAULT_MAX_LVL);
        boolean result = true;
        return result;
    }

    public boolean saveSettings(boolean isChecked, int maxLvl) {
        prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(STATS_SETTINGS, isChecked);
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

    public boolean saveSettings() {
        return saveSettings(isStats(), getMaxLvl());
    }

    public boolean saveDefaultSettings() {
        return saveSettings(true, DEFAULT_MAX_LVL);
    }

    public boolean isStats() {
        return isStats;
    }

    public void setStats(boolean isStats) {
        this.isStats = isStats;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int maxLvl) {
        this.maxLvl = maxLvl;
    }
}
