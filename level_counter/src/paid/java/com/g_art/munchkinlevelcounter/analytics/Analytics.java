package com.g_art.munchkinlevelcounter.analytics;

import android.content.Context;

import com.g_art.munchkinlevelcounter.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


/**
 * Created by agulia on 1/23/17.
 */

public class Analytics {

	private static Analytics instance;
	private static AnalyticType analyticType;
	private Tracker mTracker;

	private Analytics(Context context) {
		analyticType = AnalyticType.Google;
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			mTracker = analytics.newTracker(R.xml.app_tracker);
			mTracker.enableAdvertisingIdCollection(true);
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
				break;
			case Google:
				mTracker.send(new HitBuilders.EventBuilder()
						.setAction(eventName)
						.build());
				break;
			default:
				break;
		}
	}

	public void logEvent(AnalyticsActions action, String actionValue, String activityName) {
		switch (analyticType) {
			case Fabric:
				break;
			case Google:
				mTracker.send(new HitBuilders.EventBuilder()
						.setAction(action.name())
						.setCategory(activityName)
						.set(action.name(), actionValue)
						.build());
				break;
			default:
				break;
		}
	}

	public void logEvent(AnalyticsActions action, String activityName) {
		switch (analyticType) {
			case Fabric:
				break;
			case Google:
				mTracker.send(new HitBuilders.EventBuilder()
						.setAction(action.name())
						.setCategory(activityName)
						.build());
				break;
			default:
				break;
		}
	}
}
