package com.g_art.munchkinlevelcounter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.g_art.munchkinlevelcounter.players.Player;

import java.util.ArrayList;

/**
 * Created by G_Art on 28/7/2014.
 */
public class GameActivity extends Activity {

    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";

    final String TAG = "Munchkin";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "GameActivity onCreate");

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);
        Log.d(TAG, "List of player: " + playersList);

        Fragment fr = FragmentPlayersList.newInstance(playersList);
        getFragmentManager().beginTransaction().add(R.id.container, fr).commit();

    }

    protected void onStart() {
        super.onStart();
        Log.d(TAG, "GameActivity onStart");
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "GameActivity onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "GameActivity onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "GameActivity onStop");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "GameActivity onDestroy");
    }

}
