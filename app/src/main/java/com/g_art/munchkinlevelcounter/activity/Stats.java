package com.g_art.munchkinlevelcounter.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.adapter.StatsPageAdapter;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Stats extends FragmentActivity implements ActionBar.TabListener {

    final String TAG = "GameActivity_Munchkin_Test";
    private final String PLAYER_KEY = "playersList";
    private final String STATS_KEY = "collectStats";
    private final String BUNDLE_STATS_KEY = "bundleStats";
    private final String PREFS_PLAYERS_KEY = "players";
    public final static String PREFS_NO_DATA = "Sorry, No Data!";

    private ArrayList<Player> playersList;
    private ActionBar.Tab lvlTab, gearTab, powerTab;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;
    private ViewPager viewPager;
    private StatsPageAdapter statsAdapter;
    private ActionBar actionBar;
    private boolean statsCollect;
    private Gson gson;
    private String json;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mPrefs = getPreferences(Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        Intent intent = getIntent();
        bundle = intent.getBundleExtra(BUNDLE_STATS_KEY);
        if (bundle != null && !bundle.isEmpty()) {
            playersList = bundle.getParcelableArrayList(PLAYER_KEY);
            statsCollect = bundle.getBoolean(STATS_KEY, true);
            if (statsCollect) {
                if (playersList != null) {
                    Log.d(TAG, "StatsActivity gets playersList: " + playersList.toString());
                    clearSharedPrefs();
                    Log.d(TAG, "Deleting prev stats from shared prefs");
                    savePlayersToSharedPrefs();
                    Log.d(TAG, "Saving stats to shared prefs");
                }
            } else {
                if (getPlayersFromSharedPrefs()) {
                    Log.d(TAG, "Getting stats from shared prefs");
                } else {
                    Log.d(TAG, "No saved stats");
                }
            }
        } else {
            if (getPlayersFromSharedPrefs()) {
                Log.d(TAG, "Getting stats from shared prefs");
            } else {
                Log.d(TAG, "No saved stats");
            }
        }

        Bundle fragBundle = new Bundle();
        fragBundle.putParcelableArrayList(PLAYER_KEY, playersList);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        statsAdapter = new StatsPageAdapter(getSupportFragmentManager(), fragBundle);
        Log.d(TAG, "Setting pager adapter");

        viewPager.setAdapter(statsAdapter);

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        lvlTab = actionBar.newTab().setText(getResources().getText(R.string.lvl_tab));
        lvlTab.setTabListener(this);

        gearTab = actionBar.newTab().setText(getResources().getText(R.string.gear_tab));
        gearTab.setTabListener(this);

        powerTab = actionBar.newTab().setText(getResources().getText(R.string.power_tab));
        powerTab.setTabListener(this);

        actionBar.addTab(lvlTab);
        actionBar.addTab(gearTab);
        actionBar.addTab(powerTab);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

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
        gson = new Gson();
        boolean result;

        json = gson.toJson(playersList);
        Log.d(TAG, json);
        prefsEditor.putString(PREFS_PLAYERS_KEY, json);
        try {
            prefsEditor.apply();
            result = true;
        } catch (Exception ex) {
            result = false;
        }

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

        gson = new Gson();

        json = mPrefs.getString(PREFS_PLAYERS_KEY, PREFS_NO_DATA);

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
