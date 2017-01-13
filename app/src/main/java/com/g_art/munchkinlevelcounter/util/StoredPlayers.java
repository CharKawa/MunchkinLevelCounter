package com.g_art.munchkinlevelcounter.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.g_art.munchkinlevelcounter.model.Player;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * MunchinLevelCounter
 * Created by G_Art on 17/1/2015.
 */
public class StoredPlayers {
	private static StoredPlayers instance;
	private final String PREFS_PLAYERS_KEY = "players";
	private SharedPreferences mPrefs;
	private boolean mResult = false;
	private String json;

	private Gson gson;


	private StoredPlayers(SharedPreferences sharedPreferences) {
		this.gson = new Gson();
		this.mPrefs = sharedPreferences;
	}

	public static StoredPlayers getInstance(SharedPreferences sharedPreferences) {
		if (instance == null) {
			instance = new StoredPlayers(sharedPreferences);
		} else {
			instance.setSharedPreferences(sharedPreferences);
		}
		return instance;
	}

	/**
	 * Saving players stats to shared prefs in json format, using Gson from Google(deprecated)
	 *
	 * @return boolean. True for success, false for failure.
	 */
	public boolean savePlayers(List<Player> playersList) {
		json = gson.toJson(playersList);
		Log.i("JSON", "savePlayers: " + json);
		SharedPreferences.Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString(PREFS_PLAYERS_KEY, json);
		try {
			prefsEditor.apply();
			mResult = true;
		} catch (Exception ex) {
			mResult = false;
		}
		return mResult;
	}

	/**
	 * Getting players stats from shared prefs.
	 *
	 * @return ArrayList<Player>
	 */
	public ArrayList<Player> loadPlayers(String noData) {
		ArrayList<Player> playersList = new ArrayList<>();

		if (isPlayersStored()) {
			json = mPrefs.getString(PREFS_PLAYERS_KEY, noData);
			Log.i("JSON", "loadPlayers: " + json);
			if (!json.equals(noData)) {
				Type type = new TypeToken<ArrayList<Player>>() {
				}.getType();
				try {
					playersList = gson.fromJson(json, type);
					mResult = true;
				} catch (JsonSyntaxException jsonSyntaxEx) {
					mResult = false;
				}
			} else {
				mResult = false;
			}
		}
		return playersList;
	}

	/**
	 * Removing previous results of statistic in shared prefs before storing new one
	 *
	 * @return boolean. True for success, false for failure.
	 */
	public boolean clearStoredPlayers() {
		SharedPreferences.Editor prefsEditor = mPrefs.edit();
		if (isPlayersStored()) {
			prefsEditor.remove(PREFS_PLAYERS_KEY);
			try {
				prefsEditor.apply();
				mResult = true;
			} catch (Exception ex) {
				mResult = false;
			}
		}
		return mResult;

	}

	public boolean isPlayersStored() {
		return mPrefs.contains(PREFS_PLAYERS_KEY);
	}

	private void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.mPrefs = sharedPreferences;
	}
}
