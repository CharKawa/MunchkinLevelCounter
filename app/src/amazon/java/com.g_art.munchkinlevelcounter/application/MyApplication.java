package com.g_art.munchkinlevelcounter.application;

import android.app.Application;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by agulia on 1/23/17.
 */

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//				.setDefaultFontPath("fonts/Quasimodo_Original.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);

		Analytics.init(this);
	}
}
