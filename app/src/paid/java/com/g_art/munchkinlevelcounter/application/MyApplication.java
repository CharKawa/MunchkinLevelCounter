package com.g_art.munchkinlevelcounter.application;

import android.app.Application;

import com.g_art.munchkinlevelcounter.BuildConfig;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * MunchkinLevelCounter
 * Created by G_Art on 29/10/2014.
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		if ( BuildConfig.USE_FABRIC ) {
		}
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath(getString(R.string.font_path))
				.setFontAttrId(R.attr.fontPath)
				.build()
		);

		Analytics.init(this);
	}
}
