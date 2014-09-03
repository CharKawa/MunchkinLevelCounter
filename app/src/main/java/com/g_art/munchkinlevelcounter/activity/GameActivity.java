package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.FragmentPlayer;
import com.g_art.munchkinlevelcounter.fragments.FragmentPlayersList;
import com.g_art.munchkinlevelcounter.tasks.SavePlayersStatsTask;

import java.util.ArrayList;

/**
 * Created by G_Art on 28/7/2014.
 */
public class GameActivity extends Activity implements FragmentPlayersList.OnPlayerSelectedListener, FragmentPlayer.PlayersUpdate {

    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";
    final int FIRST_PLAYER = 0;
    private FragmentPlayersList fr;
    private static final String TAG_FPL_FRAGMENT = "Fragment_Players_List";
    private SavePlayersStatsTask saveTask;

    final String TAG = "GameActivity_Munchkin_Test";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "GameActivity onCreate");

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);
        Log.d(TAG, "List of player: " + playersList);


        FragmentManager fm = getFragmentManager();
        fr = (FragmentPlayersList) fm.findFragmentByTag(TAG_FPL_FRAGMENT);
        Log.d(TAG, "fr: " + fr);

        if (fr == null) {
            fr = new FragmentPlayersList();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PLAYER_KEY, playersList);
            fr.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragmentList, fr, TAG_FPL_FRAGMENT).commit();
        }

        /*
        Saving first players stats
         */
        savePlayersStats();

        /*
        Setting the first player chosen
         */
        onPlayerSelected(playersList.get(FIRST_PLAYER));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    public void onPlayersUpdate() {
        FragmentPlayersList fragment = (FragmentPlayersList) getFragmentManager().findFragmentById(R.id.fragmentList);
        if (fragment == null) {
            Log.d(TAG, "fragmentPlayer is NULL");
        } else {
            fragment.listUpdate();
            Log.d(TAG, "List update");
        }
    }

    @Override
    public boolean onNextTurnClick(Player player) {
        boolean result = false;
        int i = 0;
        try {
            for (Player selectedPlayer : playersList) {
                i++;
                if (selectedPlayer.equals(player)) {
                    if (i == playersList.size()) {
                        i = 0;
                    }
                    onPlayerSelected(playersList.get(i));
                    Log.d(TAG, "Sending next player");
                }
            }

            result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public boolean savePlayersStats() {
        boolean result;
        saveTask = new SavePlayersStatsTask();

        try {
            saveTask.execute(playersList);
            result = saveTask.get();

        } catch (Exception ex) {
            result = false;
        }

        return result;
    }


    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

}
