package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.LineFragment;

import java.util.ArrayList;


public class Stats extends Activity {

    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";
    private LineFragment fr;
    private FragmentManager fm;
    final String TAG = "GameActivity_Munchkin_Test";
    private static final String TAG_STATS_FRAGMENT = "Fragment_Players_Stats";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);

        fm = getFragmentManager();
        fr = (LineFragment) fm.findFragmentByTag(TAG_STATS_FRAGMENT);
        Log.d(TAG, "fr in stats activity: " + fr);

        if (fr == null) {
            fr = new LineFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PLAYER_KEY, playersList);
            fr.setArguments(bundle);
            fm.beginTransaction().add(R.id.statsContainer, fr, TAG_STATS_FRAGMENT).commit();
        }
    }

}
