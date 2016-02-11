package com.g_art.munchkinlevelcounter.util;

import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by G_Art on 12/9/2014.
 */
public class SettingsHandler {
    public static final String MAX_LVL_SETTINGS = "max_lvl_settings";
    public static final String POPUP_STATUS = "popup_status";
    public static final String STATUS_UPDATE_DATE = "status_update_date";
    public static final int DEFAULT_MAX_LVL = 10;
    public static final int MIN_LVL = 1;
    public static final int ASK_LATER = 1;
    public static final int FIRST_SHOW = 0;
    public static final int NEVER_ASK = 2;
    private static SettingsHandler settingsInstance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private int maxLvl = DEFAULT_MAX_LVL;
    private int popupStatus = FIRST_SHOW;
    private Date statusUpdate;

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

    public int getPopupStatus() {
        if (!mPrefs.contains(POPUP_STATUS)) {
            updateStatus(FIRST_SHOW);
        }
        popupStatus = mPrefs.getInt(POPUP_STATUS, FIRST_SHOW);
        return popupStatus;
    }

    public Date getStatusDate() {
        if (!mPrefs.contains(STATUS_UPDATE_DATE)) {
            updateStatus(FIRST_SHOW);
        }
        long dateTime = mPrefs.getLong(STATUS_UPDATE_DATE, new Date().getTime());
        return new Date(dateTime);
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

    public boolean updateStatus(int popupStatus) {
        prefsEditor = mPrefs.edit();
        prefsEditor.putInt(POPUP_STATUS, popupStatus);
        prefsEditor.putLong(STATUS_UPDATE_DATE, new Date().getTime());
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
