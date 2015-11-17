package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.dialog.ConfirmDialog;
import com.g_art.munchkinlevelcounter.fragments.dialog.DiceDialog;
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
    final String BUNDLE_STATS_KEY = "bundleStats";
    final int FIRST_PLAYER = 0;
    private ArrayList<Player> playersList;
    private FragmentPlayersList fr;
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
        onPlayerSelected(FIRST_PLAYER);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.in_game, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_dice:
                rollADice();
                return true;
            case R.id.action_finish:
                savePlayersStats();
                finishGame();
                return true;
            case R.id.action_settings:
                //todo show dialog with max lvl
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onPlayerSelected(int position) {

        FragmentPlayer fragmentPlayer = (FragmentPlayer) fm.findFragmentById(R.id.currentPlayer_Fragment);

        if (fragmentPlayer != null) {
            fragmentPlayer.changeSelectedPlayer(playersList.get(position));
            scrollToPlayer(position);
        }
    }

    @Override
    public void finishClick() {
        savePlayersStats();
        openStatsActivity();
    }

    @Override
    public void onPlayersUpdate() {
        FragmentPlayersList fragment = (FragmentPlayersList) fm.findFragmentById(R.id.fragmentList);
        if (fragment != null) {
            fragment.listUpdate();
        }
    }

    private void scrollToPlayer(int position) {

        FragmentPlayersList fragment = (FragmentPlayersList) fm.findFragmentById(R.id.fragmentList);
        if (fragment != null) {
            fragment.scrollToPlayer(position);
        }
    }

    @Override
    public boolean onNextTurnClick(Player player) {
        boolean result = false;
        try {
            int i = 0;
            for (Player selectedPlayer : playersList) {
                i++;
                if (selectedPlayer.equals(player)) {
                    if (i == playersList.size()) {
                        i = 0;
                    }
                    onPlayerSelected(i);
                }
            }

            result = true;
        } catch (Exception ex) {
            result = false;
            throw ex;
        }
        return result;
    }

    @Override
    public int maxLvl() {
        return maxLvl;
    }

    public boolean savePlayersStats() {
        boolean result;
        SavePlayersStatsTask saveTask = new SavePlayersStatsTask();

        try {
            saveTask.execute(playersList);
            result = saveTask.get();

        } catch (Exception ex) {
            result = false;
        }

        return result;
    }

    private void rollADice() {
        int resId = 1;
        int Min = 1;
        int Max = 6;
        int dice = Min + (int) (Math.random() * ((Max - Min) + 1));
        switch (dice) {
            case 1:
                resId = R.drawable.dice_1;
                break;
            case 2:
                resId = R.drawable.dice_2;
                break;
            case 3:
                resId = R.drawable.dice_3;
                break;
            case 4:
                resId = R.drawable.dice_4;
                break;
            case 5:
                resId = R.drawable.dice_5;
                break;
            case 6:
                resId = R.drawable.dice_6;
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(DiceDialog.DICE_KEY, resId);
        DiceDialog diceDialog = new DiceDialog();
        diceDialog.setArguments(bundle);
        diceDialog.show(fm, "dice");
    }

    private void finishGame() {
        Bundle bundle = new Bundle();
        bundle.putString(ConfirmDialog.SOURCE_KEY, "FragmentPlayer");
        bundle.putInt(ConfirmDialog.TITLE_KEY, R.string.title_dialog_finish);
        bundle.putInt(ConfirmDialog.MSG_KEY, R.string.msg_finish_game);
        bundle.putInt(ConfirmDialog.OK_KEY, R.string.ok_btn_for_dialog_finish);
        bundle.putInt(ConfirmDialog.NOT_KEY, R.string.cancel_btn_for_dialog_finish);
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setArguments(bundle);
        confirmDialog.show(fm, "confirmDialog");
    }

    public void onPositiveClickContinueDialog() {
        openStatsActivity();
    }

    private void openStatsActivity() {
        Intent intent = new Intent(this, Stats.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PLAYER_KEY, getPlayersList());
        intent.putExtra(BUNDLE_STATS_KEY, bundle);
        startActivity(intent);
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int updateMaxLVL(int newMaxLVL) {
        if (settingsHandler.saveSettings(newMaxLVL)) {
            //todo toast OK
        }

        return settingsHandler.getMaxLvl();
    }
}
