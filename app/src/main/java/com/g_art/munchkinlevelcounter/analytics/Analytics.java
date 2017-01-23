package com.g_art.munchkinlevelcounter.analytics;

/**
 * Created by agulia on 1/23/17.
 */

public class Analytics {

	private static Analytics instance;

	private Analytics() {

	}

	public Analytics getInstance() {
		if (instance == null) {
			instance = new Analytics();
		}
		return instance;
	}
}
