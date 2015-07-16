package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.dialog.ConfirmDialog;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayer;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayersList;
import com.g_art.munchkinlevelcounter.util.SavePlayersStatsTask;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;

import java.util.ArrayList;

/**
 * Created by G_Art on 28/7/2014.
 */
public class GameActivity extends Activity implements FragmentPlayersList.OnPlayerSelectedListener, FragmentPlayer.PlayersUpdate {

    private static final String TAG_FPL_FRAGMENT = "Fragment_Players_List";
    final String PLAYER_KEY = "playersList";
    final int FIRST_PLAYER = 0;
    private ArrayList<Player> playersList;
    private FragmentPlayersList fr;
    private SavePlayersStatsTask saveTask;
    private FragmentManager fm;
    private ConfirmDialog confirmDialog;
    private SharedPreferences mPrefs;
    private SettingsHandler settingsHandler;

    private int maxLvl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_game);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        settingsHandler = SettingsHandler.getInstance(mPrefs);

        settingsHandler.loadSettings();
        maxLvl = settingsHandler.getMaxLvl();

        Intent intent = getIntent();
        playersList = intent.getParcelableArrayListExtra(PLAYER_KEY);

        fm = getFragmentManager();
        fr = (FragmentPlayersList) fm.findFragmentByTag(TAG_FPL_FRAGMENT);

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
        Bundle bundle = new Bundle();
        bundle.putString(ConfirmDialog.SOURCE_KEY, "GameActivity");
        bundle.putInt(ConfirmDialog.TITLE_KEY, R.string.title_dialog_confirm);
        bundle.putInt(ConfirmDialog.MSG_KEY, R.string.message_for_dialog_confirm);
        bundle.putInt(ConfirmDialog.OK_KEY, R.string.ok_btn_for_dialog_confirm);
        bundle.putInt(ConfirmDialog.NOT_KEY, R.string.cancel_btn_for_dialog_confirm);
        confirmDialog = new ConfirmDialog();
        confirmDialog.setArguments(bundle);
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

        if (fragmentPlayer != null) {
            fragmentPlayer.changeSelectedPlayer(player);
        }

    }

    @Override
    public void onPlayersUpdate() {
        FragmentPlayersList fragment = (FragmentPlayersList) fm.findFragmentById(R.id.fragmentList);
        if (fragment != null) {
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
    public int maxLvl() {
        return maxLvl;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
