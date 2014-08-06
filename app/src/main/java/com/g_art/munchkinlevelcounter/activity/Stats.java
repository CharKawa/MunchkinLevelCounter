package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.LineFragment;

import java.util.ArrayList;


public class Stats extends Activity {

    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);

        Fragment fr = new LineFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PLAYER_KEY, playersList);
        fr.setArguments(bundle);

        getFragmentManager().beginTransaction().add(R.id.statsContainer, fr).commit();
    }

}
