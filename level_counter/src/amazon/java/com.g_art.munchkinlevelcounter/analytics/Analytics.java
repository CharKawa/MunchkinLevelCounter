package com.g_art.munchkinlevelcounter.analytics;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.g_art.munchkinlevelcounter.BuildConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by agulia on 1/23/17.
 */

public class Analytics {

	private static Analytics instance;
	private static AnalyticType analyticType;

	private Analytics(Context context) {
		if (BuildConfig.USE_FABRIC) {
			final Fabric fabric = new Fabric.Builder(context)
					.kits(new Crashlytics())
					.debuggable(true)
					.build();
			Fabric.with(fabric);
			analyticType = AnalyticType.Fabric;
		} else {
			//do nothing
		}
	}

	public static void init(Context context) {
		instance = new Analytics(context);
	}

	public static Analytics getInstance() {
		return instance;
	}

	public void logEvent(String eventName) {
		switch (analyticType) {
			case Fabric:
				Answers.getInstance().logCustom(new CustomEvent(eventName));
				break;
			case Google:
				break;
			default:
				break;
		}
	}

	public void logEvent(AnalyticsActions action, String activityName) {
		logEvent(action, "actionValue", activityName);
	}

	public void logEvent(AnalyticsActions action, String actionValue, String activityName) {
		logEvent(action.name());
	}
}
