package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.FragmentPlayer;
import com.g_art.munchkinlevelcounter.fragments.FragmentPlayersList;

import java.util.ArrayList;

/**
 * Created by G_Art on 28/7/2014.
 */
public class GameActivity extends Activity implements FragmentPlayersList.OnPlayerSelectedListener, FragmentPlayer.PlayersListUpdate {

    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";
    final int FIRST_PLAYER = 0;

    final String TAG = "Munchkin";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "GameActivity onCreate");

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);
        Log.d(TAG, "List of player: " + playersList);

        Fragment fr = new FragmentPlayersList();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PLAYER_KEY, playersList);
        fr.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragmentList, fr).commit();

        onPlayerSelected(playersList.get(FIRST_PLAYER));
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
        Log.d(TAG, "GameActivity onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPlayerSelected(Player player) {

        FragmentPlayer fragmentPlayer = (FragmentPlayer) getFragmentManager().findFragmentById(R.id.currentPlayer_Fragment);

        if (fragmentPlayer == null) {
            Log.d(TAG, "fragmentPlayer is NULL");
        } else {
            fragmentPlayer.changeSelectedPlayer(player);
            Log.d(TAG, "Selected player on position: " + player);
        }

    }

    @Override
    public void onPlayersListUpdate() {
        FragmentPlayersList fragment = (FragmentPlayersList) getFragmentManager().findFragmentById(R.id.fragmentList);
        if (fragment == null) {
            Log.d(TAG, "fragmentPlayer is NULL");
        } else {
            fragment.listUpdate();
            Log.d(TAG, "List update");
        }
    }
}
