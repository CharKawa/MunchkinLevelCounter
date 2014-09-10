package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.FragmentPlayer;
import com.g_art.munchkinlevelcounter.fragments.FragmentPlayersList;
import com.g_art.munchkinlevelcounter.fragments.dialog.ConfirmDialog;
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
    private FragmentManager fm;
    private ConfirmDialog confirmDialog;
    private SharedPreferences mPrefs;

    private boolean collectStats;

    final String TAG = "GameActivity_Munchkin_Test";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (mPrefs.contains(Settings.STATS_SETTINGS)) {
            collectStats = mPrefs.getBoolean(Settings.STATS_SETTINGS, true);
        }

        confirmDialog = new ConfirmDialog();

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);

        fm = getFragmentManager();
        fr = (FragmentPlayersList) fm.findFragmentByTag(TAG_FPL_FRAGMENT);

        if (fr == null) {
            fr = new FragmentPlayersList();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PLAYER_KEY, playersList);
            bundle.putBoolean(Settings.STATS_SETTINGS, collectStats);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        confirmDialog.show(fm, "confirmDialog");
    }

    public void onPositiveClickConfirmDialog() {
        super.onBackPressed();
    }

    public void onNegativeClickConfirmDialog() {
        confirmDialog.dismiss();
    }


    @Override
    public void onPlayerSelected(Player player) {

        FragmentPlayer fragmentPlayer = (FragmentPlayer) fm.findFragmentById(R.id.currentPlayer_Fragment);

        if (fragmentPlayer == null) {
        } else {
            fragmentPlayer.changeSelectedPlayer(player);
        }

    }

    @Override
    public void onPlayersUpdate() {
        FragmentPlayersList fragment = (FragmentPlayersList) fm.findFragmentById(R.id.fragmentList);
        if (fragment == null) {
        } else {
            fragment.listUpdate();
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
                }
            }

            result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean collectStats() {
        return collectStats;
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
