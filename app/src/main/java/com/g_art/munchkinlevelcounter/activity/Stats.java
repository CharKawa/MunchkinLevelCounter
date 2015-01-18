package com.g_art.munchkinlevelcounter.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.adapter.StatsPageAdapter;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;

import java.util.ArrayList;


public class Stats extends FragmentActivity implements ActionBar.TabListener {

    final String TAG = "GameActivity_Munchkin_Test";
    private final String PLAYER_KEY = "playersList";
    private final String STATS_KEY = "collectStats";
    private final String BUNDLE_STATS_KEY = "bundleStats";
    private String PREFS_NO_DATA;

    private ArrayList<Player> playersList;

    private ViewPager viewPager;
    private ActionBar actionBar;
    private StoredPlayers mStored;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ActionBar.Tab lvlTab, gearTab, powerTab;

        mStored = StoredPlayers.getInstance(PreferenceManager.getDefaultSharedPreferences(getBaseContext()));

        PREFS_NO_DATA = getString(R.string.no_data);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BUNDLE_STATS_KEY);
        if (bundle != null && !bundle.isEmpty()) {
            playersList = bundle.getParcelableArrayList(PLAYER_KEY);
            boolean statsCollect = bundle.getBoolean(STATS_KEY, true);
            if (statsCollect) {
                if (playersList != null) {
                    mStored.clearStoredPlayers();
                    mStored.savePlayers(playersList);
                }
            } else {
                Toast.makeText(this, getString(R.string.prev_stas), Toast.LENGTH_SHORT).show();
                playersList = mStored.loadPlayers(PREFS_NO_DATA);
            }
        } else {
            playersList = mStored.loadPlayers(PREFS_NO_DATA);
        }

        Bundle fragBundle = new Bundle();
        fragBundle.putParcelableArrayList(PLAYER_KEY, playersList);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        StatsPageAdapter statsAdapter = new StatsPageAdapter(getSupportFragmentManager(), fragBundle);
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


    /**
     * Navigates to parent activity
     */
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
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
}
