package com.g_art.munchkinlevelcounter.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by agulia on 2/3/17.
 */

public class PreferenceScreen extends AppCompatPreferenceActivity {

	public static final String KEY_PREF_MAX_LEVEL = "pref_max_lvl";
	public static final String KEY_PREF_SCREEN_ON = "pref_screen_on";
	public static final int DEFAULT_MAX_LVL = 10;
	public static final String DEFAULT_MAX_LVL_STRING = "10";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
