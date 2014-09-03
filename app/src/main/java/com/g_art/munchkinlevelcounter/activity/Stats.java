package com.g_art.munchkinlevelcounter.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.listeners.StatsTabListener;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.stats.GearStatsFragment;
import com.g_art.munchkinlevelcounter.fragments.stats.LvlStatsFragment;
import com.g_art.munchkinlevelcounter.fragments.stats.PowerStatsFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Stats extends Activity {

    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";
    final static String PREFS_PLAYERS_KEY = "players";
    public final static String PREFS_NO_DATA = "Sorry, No Data!";
    private ActionBar.Tab lvlTab, gearTab, powerTab;
    private LvlStatsFragment lvlFr;
    private GearStatsFragment gearFr;
    private PowerStatsFragment powerFr;
    private FragmentManager fm;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;
    final String TAG = "GameActivity_Munchkin_Test";
    private static final String TAG_STATS_FRAGMENT = "Fragment_Players_Stats";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mPrefs = getPreferences(Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);

        if (playersList != null) {
            Log.d(TAG, "StatsActivity gets playersList: " + playersList.toString());
            clearSharedPrefs();
            Log.d(TAG, "Deleting prev stats from shared prefs");
            savePlayersToSharedPrefs();
            Log.d(TAG, "Saving stats to shared prefs");
        } else {
            getPlayersFromSharedPrefs();
            Log.d(TAG, "Getting stats from shared prefs");
        }


        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PLAYER_KEY, playersList);

        fm = getFragmentManager();
        lvlFr = (LvlStatsFragment) fm.findFragmentById(R.id.statsFram);

        if (lvlFr == null) {
            Log.d(TAG, "lvlFR is null");
            lvlFr = new LvlStatsFragment();
            lvlFr.setArguments(bundle);
        }

        gearFr = (GearStatsFragment) fm.findFragmentById(R.id.statsFram);

        if (gearFr == null) {
            Log.d(TAG, "gearFr is null");
            gearFr = new GearStatsFragment();
            gearFr.setArguments(bundle);
        }

        powerFr = (PowerStatsFragment) fm.findFragmentById(R.id.statsFram);

        if (powerFr == null) {
            Log.d(TAG, "powerFr is null");
            powerFr = new PowerStatsFragment();
            powerFr.setArguments(bundle);
        }


        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        lvlTab = actionBar.newTab().setText(getResources().getText(R.string.lvl_tab));
        lvlTab.setTabListener(new StatsTabListener(lvlFr));

        gearTab = actionBar.newTab().setText(getResources().getText(R.string.gear_tab));
        gearTab.setTabListener(new StatsTabListener(gearFr));

        powerTab = actionBar.newTab().setText(getResources().getText(R.string.power_tab));
        powerTab.setTabListener(new StatsTabListener(powerFr));

        actionBar.addTab(lvlTab);
        actionBar.addTab(gearTab);
        actionBar.addTab(powerTab);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * Removing previous results of statistic in shared prefs before storing new one
     *
     * @return boolean. True for success, false for failure.
     */
    private boolean clearSharedPrefs() {
        boolean result;
        prefsEditor.remove(PREFS_PLAYERS_KEY);
        try {
            prefsEditor.apply();
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;

    }

    /**
     * Saving players stats to shared prefs in json format, using Gson from Google(deprecated)
     *
     * @return boolean. True for success, false for failure.
     */
    private boolean savePlayersToSharedPrefs() {
        Gson gson = new Gson();
        boolean result;

        String json = gson.toJson(playersList);
        Log.d(TAG, json);
        prefsEditor.putString(PREFS_PLAYERS_KEY, json);
        result = prefsEditor.commit();


        return result;
    }

    /**
     * Getting players stats from shared prefs.
     *
     * @return boolean. True for success, false for failure.
     */
    private boolean getPlayersFromSharedPrefs() {
        boolean result = false;
        playersList = new ArrayList<Player>();
        Gson gson = new Gson();
        String json = mPrefs.getString(PREFS_PLAYERS_KEY, PREFS_NO_DATA);
        if (!json.equals(PREFS_NO_DATA)) {
            Type type = new TypeToken<ArrayList<Player>>() {
            }.getType();
            try {
                playersList = gson.fromJson(json, type);
                Log.d(TAG, playersList.toString());
                result = true;
            } catch (JsonSyntaxException jsonSyntaxEx) {
                Log.d(TAG, jsonSyntaxEx.toString());
                result = false;
            } catch (IllegalStateException illegalStateEx) {
                Log.d(TAG, illegalStateEx.toString());
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }



}
