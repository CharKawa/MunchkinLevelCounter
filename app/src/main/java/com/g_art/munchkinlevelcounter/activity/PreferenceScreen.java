package com.g_art.munchkinlevelcounter.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by agulia on 2/3/17.
 */

public class PreferenceScreen extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String KEY_PREF_MAX_LEVEL = "pref_max_lvl";
	public static final String KEY_PREF_SCREEN_ON = "pref_max_lvl";
	public static final int DEFAULT_MAX_LVL = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
										  String key) {
		if (key.equals(KEY_PREF_MAX_LEVEL)) {
			Preference connectionPref = findPreference(key);
			// Set summary to be the user-description for the selected value
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
}
