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
                selectedPlayer.getLvlStats().add(String.valueOf(selectedPlayer.getLevel()));
                selectedPlayer.getGearStats().add(String.valueOf(selectedPlayer.getGear()));
                selectedPlayer.getPowerStats().add(String.valueOf(selectedPlayer.getLevel() + selectedPlayer.getGear()));
                Log.d(TAG, " Stas from player: " + selectedPlayer.getName() + " :" + selectedPlayer.getLvlStats().toString());
                Log.d(TAG, " Stas from player: " + selectedPlayer.getName() + " :" + selectedPlayer.getGearStats().toString());
                Log.d(TAG, " Stas from player: " + selectedPlayer.getName() + " :" + selectedPlayer.getPowerStats().toString());
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
        } finally {
            return result;
        }
    }


    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

}
