package com.g_art.munchkinlevelcounter.application;

import android.app.Application;

import com.g_art.munchkinlevelcounter.BuildConfig;
import com.g_art.munchkinlevelcounter.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * MunchkinLevelCounter
 * Created by G_Art on 29/10/2014.
 */
public class MyApplication extends Application {
	private Tracker mTracker;

	/**
	 * Gets the default {@link Tracker} for this {@link Application}.
	 *
	 * @return tracker
	 */
	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			mTracker = analytics.newTracker(R.xml.app_tracker);
			mTracker.enableAdvertisingIdCollection(true);
		}
		return mTracker;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/Quasimodo.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
	}
}
