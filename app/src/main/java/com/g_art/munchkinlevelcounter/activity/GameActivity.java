package com.g_art.munchkinlevelcounter.activity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.fragments.dialog.ConfirmDialog;
import com.g_art.munchkinlevelcounter.fragments.dialog.MaxLvlDialog;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayer;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayersList;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.util.SavePlayersStatsTask;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

/**
 * Created by G_Art on 28/7/2014.
 */
public class GameActivity extends AppCompatActivity implements FragmentPlayersList.OnPlayerSelectedListener, FragmentPlayer.PlayersUpdate {
    public final static String CURR_LVL = "currentLVL";
	public static final String PLAYER = "player";
	public static final String PLAYERS_KEY = "playersList";
	private static final String TAG_FPL_FRAGMENT = "Fragment_Players_List";
	public static final int BATTLE_RESULT_OK = 1;
	public static final int BATTLE_RESULT_CANCEL = 0;
	private static final String SELECTED_KEY = "selectedPlayer";
	private final int BATTLE_REQUEST = 10;

    private Tracker mTracker;
    private FragmentManager fm;
    private ConfirmDialog confirmDialog;
    private DialogFragment lvlDialog;
    private SettingsHandler settingsHandler;

    private int maxLvl;
	private ArrayList<Player> playersList;
	private int mSelectedPlayerPosition;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_game);

		fm = getFragmentManager();

		if (savedInstanceState != null) {
			playersList = savedInstanceState.getParcelableArrayList(PLAYERS_KEY);
			mSelectedPlayerPosition = savedInstanceState.getInt(SELECTED_KEY);
		} else {
			Intent intent = getIntent();
			playersList = intent.getParcelableArrayListExtra(PLAYERS_KEY);
			/*
        	Saving first players stats
         	*/
			savePlayersStats();

        	/*
        	Setting the first player chosen
         	*/
			mSelectedPlayerPosition = 0;
		}

		FragmentPlayersList fr = (FragmentPlayersList) fm.findFragmentByTag(TAG_FPL_FRAGMENT);

		if (fr == null) {
			fr = new FragmentPlayersList();
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(PLAYERS_KEY, playersList);
			fr.setArguments(bundle);
			fm.beginTransaction().add(R.id.fragmentList, fr, TAG_FPL_FRAGMENT).commit();
		}

        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setAction("GameStarted")
                .setCategory("Screen")
                .setLabel("GameActivity")
                .build());

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        settingsHandler = SettingsHandler.getInstance(mPrefs);

        settingsHandler.loadSettings();
        maxLvl = settingsHandler.getMaxLvl();

		onPlayerSelected(mSelectedPlayerPosition);

    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(PLAYERS_KEY, playersList);
		outState.putInt(SELECTED_KEY, mSelectedPlayerPosition);
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
                showMaxLvLDialog();
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
        mTracker.send(new HitBuilders.EventBuilder()
                .setAction("GameTerminated")
                .setLabel("GameActivity")
                .build());
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
			mSelectedPlayerPosition = position;
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
            mTracker.send(new HitBuilders.EventBuilder()
                    .setAction("GameTerminated")
                    .setLabel("GameActivity").set("ERROR", ex.toString())
                    .build());
        }
        return result;
    }

    @Override
    public int maxLvl() {
        return settingsHandler.getMaxLvl();
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
        mTracker.send(new HitBuilders.EventBuilder()
                .setAction("DiceRolled")
                .setLabel("GameActivity")
                .build());
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
//
//        Bundle bundle = new Bundle();
//        bundle.putInt(DiceDialog.DICE_KEY, resId);
//        DiceDialog diceDialog = new DiceDialog();
//        diceDialog.setArguments(bundle);
//        diceDialog.show(fm, "dice");
        showBattle();
    }

	private void showBattle() {
		//todo launch battle activity for result
		Intent intent = new Intent(this, BattleActivity.class);
		intent.putExtra(PLAYER, mSelectedPlayerPosition);
		intent.putParcelableArrayListExtra(PLAYERS_KEY, playersList);
		intent.putExtra(CURR_LVL, maxLvl());
		View view = findViewById(R.id.action_dice);
		int x = (int) view.getX();
		int y = (int) view.getY();
		ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, x, y, 0, 0);
		startActivityForResult(intent, BATTLE_REQUEST, optionsCompat.toBundle());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (BATTLE_REQUEST == requestCode) {
			if (BATTLE_RESULT_OK == resultCode) {

			} else if (BATTLE_RESULT_CANCEL == resultCode) {

			} else {

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
        bundle.putParcelableArrayList(PLAYERS_KEY, getPlayersList());
        String BUNDLE_STATS_KEY = "bundleStats";
        intent.putExtra(BUNDLE_STATS_KEY, bundle);
        startActivity(intent);
    }

    private ArrayList<Player> getPlayersList() {
        return playersList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showMaxLvLDialog() {
        Bundle nBundle = new Bundle();
        nBundle.putInt(CURR_LVL, maxLvl);
        if (lvlDialog == null) {
            lvlDialog = new MaxLvlDialog();
        }
        lvlDialog.setArguments(nBundle);
        lvlDialog.show(getFragmentManager(), "LvLDialog");
    }

    public void doPositiveClickLvLDialog(int newMaxLvl) {
        maxLvl = updateMaxLVL(newMaxLvl);
    }

    private int updateMaxLVL(int newMaxLVL) {
        if (settingsHandler.saveSettings(newMaxLVL)) {
            Toast.makeText(this,
                    getString(R.string.settings_saved),
                    Toast.LENGTH_SHORT
            ).show();
        }
        return settingsHandler.getMaxLvl();
    }
}
