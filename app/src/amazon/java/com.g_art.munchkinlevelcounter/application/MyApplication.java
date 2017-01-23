package com.g_art.munchkinlevelcounter.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.g_art.munchkinlevelcounter.BuildConfig;
import com.g_art.munchkinlevelcounter.R;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by agulia on 1/23/17.
 */

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/Quasimodo.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);

		if (BuildConfig.USE_FABRIC) {
			final Fabric fabric = new Fabric.Builder(this)
					.kits(new Crashlytics())
					.debuggable(true)
					.build();
			Fabric.with(fabric);
		}
	}
}
