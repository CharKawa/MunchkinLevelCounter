package com.g_art.munchkinlevelcounter.util;

/**
 * Created by agulia on 2/8/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.g_art.munchkinlevelcounter.activity.PreferenceScreen;

/**
 * It is needed to migrate old settings version to new one
 * Must be called from Application on create once.
 * Need to check settings version
 */
public class SettingsMigration {
	public static String SETTINGS_VERSION_KEY = "settings_version";
	public static int CURRENT_SETTINGS_VERSION = 2;
	public static int PREV_SETTINGS_VERSION = 1;
	public static int DEFAULT_SETTINGS_VERSION = -1;

	public static void startMigration(Context baseContext) {
		final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(baseContext);
		final int settingsVersion = mPrefs.getInt(SETTINGS_VERSION_KEY, DEFAULT_SETTINGS_VERSION);
		if (CURRENT_SETTINGS_VERSION == settingsVersion) {
			return;
		}
		if (DEFAULT_SETTINGS_VERSION == settingsVersion) {//Starting Migration
			final int maxLevel = mPrefs.getInt(SettingsHandler.MAX_LVL_SETTINGS, PreferenceScreen.DEFAULT_MAX_LVL);

			mPrefs.edit().remove(SettingsHandler.MAX_LVL_SETTINGS).apply();
			mPrefs.edit().remove(PreferenceScreen.KEY_PREF_MAX_LEVEL).apply();

			mPrefs.edit().putString(PreferenceScreen.KEY_PREF_MAX_LEVEL, Integer.toString(maxLevel)).apply();
			mPrefs.edit().putInt(SETTINGS_VERSION_KEY, CURRENT_SETTINGS_VERSION).apply();
		}
		if (PREV_SETTINGS_VERSION == settingsVersion) {
			final int maxLevel = mPrefs.getInt(PreferenceScreen.KEY_PREF_MAX_LEVEL, PreferenceScreen.DEFAULT_MAX_LVL);
			mPrefs.edit().remove(PreferenceScreen.KEY_PREF_MAX_LEVEL).apply();
			mPrefs.edit().putString(PreferenceScreen.KEY_PREF_MAX_LEVEL, Integer.toString(maxLevel)).apply();
			mPrefs.edit().putInt(SETTINGS_VERSION_KEY, CURRENT_SETTINGS_VERSION).apply();
		}

	}
}
