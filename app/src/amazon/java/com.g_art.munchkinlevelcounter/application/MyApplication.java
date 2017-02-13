package com.g_art.munchkinlevelcounter.application;

import android.app.Application;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;
import com.g_art.munchkinlevelcounter.util.SettingsMigration;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by agulia on 1/23/17.
 */

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath(getString(R.string.font_path))
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
		Analytics.init(this);

		SettingsMigration.startMigration(getBaseContext());
	}
}
