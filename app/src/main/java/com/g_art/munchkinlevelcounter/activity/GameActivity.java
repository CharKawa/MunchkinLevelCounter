package com.g_art.munchkinlevelcounter.activity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.fragments.dialog.ConfirmDialog;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayer;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayersList;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.util.SavePlayersStatsTask;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by G_Art on 28/7/2014.
 */
public class GameActivity extends AppCompatActivity
		implements
		FragmentPlayersList.OnPlayerSelectedListener,
		FragmentPlayer.PlayersUpdate {

    public final static String MAX_LVL = "max_lvl";
	public static final String PLAYER = "player";
	public static final String PLAYERS_KEY = "playersList";
	private static final String TAG_FPL_FRAGMENT = "Fragment_Players_List";
	public static final int BATTLE_REQUEST = 10;
	public static final int BATTLE_RESULT_OK = 1;
	public static final int BATTLE_RESULT_CANCEL = 0;
	public static final int BATTLE_RESULT_FAIL = 4;
	public static final int RUN_AWAY_RESULT_OK = 2;
	public static final int RUN_AWAY_RESULT_FAIL = 3;
	private static final String SELECTED_KEY = "selectedPlayer";

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
		new MaterialDialog.Builder(this)
				.title(R.string.title_dialog_confirm)
				.content(R.string.message_for_dialog_confirm)
				.positiveText(R.string.ok_btn_for_dialog_confirm)
				.negativeText(R.string.cancel_btn_for_dialog_confirm)
				.backgroundColor(getResources().getColor(R.color.background))
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						stayInTheGame();
					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						leaveGame();
					}
				})
				.autoDismiss(true)
				.show();
    }

    public void leaveGame() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setAction("GameTerminated")
                .setLabel("GameActivity")
                .build());
        super.onBackPressed();
    }

    public void stayInTheGame() {
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
        int Min = 1;
        int Max = 6;
        int dice = Min + (int) (Math.random() * ((Max - Min) + 1));
		final View v = View.inflate(this, R.layout.dice_dialog, null);
		final ImageView imageView = (ImageView) v.findViewById(R.id.imgDice);

		switch (dice) {
            case 1:
				imageView.setImageResource(R.drawable.dice_1);
                break;
            case 2:
				imageView.setImageResource(R.drawable.dice_2);
                break;
            case 3:
				imageView.setImageResource(R.drawable.dice_3);
                break;
            case 4:
				imageView.setImageResource(R.drawable.dice_4);
                break;
            case 5:
				imageView.setImageResource(R.drawable.dice_5);
                break;
            case 6:
				imageView.setImageResource(R.drawable.dice_6);
                break;
        }

//		new MaterialDialog.Builder(this)
//				.title(R.string.dice)
//				.customView(imageView, false)
//				.backgroundColor(getResources().getColor(R.color.background))
//				.autoDismiss(true)
//				.show();
        showBattle();
    }

	private void showBattle() {
		final Intent intent = new Intent(this, BattleActivity.class);
		final Bundle bundle = new Bundle();
		bundle.putInt(PLAYER, mSelectedPlayerPosition);
		bundle.putParcelableArrayList(PLAYERS_KEY, playersList);
		bundle.putInt(MAX_LVL, maxLvl());
		intent.putExtras(bundle);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			View view = findViewById(R.id.action_dice);
			int x = (int) view.getX();
			int y = (int) view.getY();ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, x, y, 0, 0);
			startActivityForResult(intent, BATTLE_REQUEST, optionsCompat.toBundle());
		} else {
			startActivityForResult(intent, BATTLE_REQUEST);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case BATTLE_RESULT_CANCEL:
				break;
			case BATTLE_RESULT_FAIL:
				break;
			case BATTLE_RESULT_OK:
				break;
			case RUN_AWAY_RESULT_FAIL:
				break;
			case RUN_AWAY_RESULT_OK:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void finishGame() {
		new MaterialDialog.Builder(this)
				.title(R.string.title_dialog_finish)
				.content(R.string.msg_finish_game)
				.positiveText(R.string.ok_btn_for_dialog_finish)
				.negativeText(R.string.cancel_btn_for_dialog_finish)
				.backgroundColor(getResources().getColor(R.color.background))
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						finishCurrentGame();
					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						contCurrentGame();
					}
				})
				.autoDismiss(true)
				.show();
    }

    public void finishCurrentGame() {
        openStatsActivity();
    }

    public void contCurrentGame() {
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
		View v = View.inflate(this, R.layout.maxlvl_dialog, null);
		final EditText maxLvlEditText = (EditText) v.findViewById(R.id.maxLvL);
		maxLvlEditText.setText(Integer.toString(maxLvl()), TextView.BufferType.EDITABLE);

		final MaterialDialog maxLvlDialog = new MaterialDialog.Builder(this)
				.title(R.string.txt_max_lvl)
				.customView(maxLvlEditText, false)
				.positiveText(R.string.dialog_ok_btn)
				.negativeText(R.string.dialog_cancel_btn)
				.backgroundColor(getResources().getColor(R.color.background))
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						EditText editLvl = (EditText)dialog.getCustomView();
						String lvl = editLvl.getText().toString();
						if (!lvl.equals("") && !lvl.equals(" ")) {
							try {
								int mLvl = Integer.parseInt(lvl);
								if (mLvl <= SettingsHandler.MIN_LVL) {
									Toast.makeText(dialog.getContext(),
											getString(R.string.error_max_level_one),
											Toast.LENGTH_SHORT
									).show();
									editLvl.setText(Integer.toString(maxLvl()), TextView.BufferType.EDITABLE);
								} else {
									doPositiveClickLvLDialog(mLvl);
								}
							} catch (NumberFormatException ex) {
								Toast.makeText(dialog.getContext(),
										getString(R.string.error_max_lvl_settings),
										Toast.LENGTH_SHORT
								).show();
							}
						}
					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						contCurrentGame();
					}
				})
		.build();

		maxLvlEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					maxLvlDialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
				} else {
					maxLvlDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		maxLvlDialog.show();
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

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
}
