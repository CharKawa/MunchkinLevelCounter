package com.g_art.munchkinlevelcounter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.model.Monster;
import com.g_art.munchkinlevelcounter.model.Player;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.g_art.munchkinlevelcounter.activity.GameActivity.BATTLE_RESULT_CANCEL;
import static com.g_art.munchkinlevelcounter.activity.GameActivity.BATTLE_RESULT_FAIL;
import static com.g_art.munchkinlevelcounter.activity.GameActivity.BATTLE_RESULT_OK;
import static com.g_art.munchkinlevelcounter.activity.GameActivity.PLAYER;
import static com.g_art.munchkinlevelcounter.activity.GameActivity.RUN_AWAY_RESULT_FAIL;
import static com.g_art.munchkinlevelcounter.activity.GameActivity.RUN_AWAY_RESULT_OK;

/**
 * MunchkinLevelCounter
 * Created by fftem on 03-Aug-16.
 */

public class BattleActivity extends AppCompatActivity {

	private static final int SUCCESS_RUN_AWAY = 4;

	private static final String PLAYERS_KEY = "helpers";
	//Player's views
	@BindView(R.id.txt_battle_player_name)
	TextView pName;
	@BindView(R.id.txt_battle_lvl_value)
	TextView pLvl;
	@BindView(R.id.txt_battle_gear_value)
	TextView pGear;
	@BindView(R.id.txt_battle_mods_value)
	TextView pMods;
	@BindView(R.id.txt_battle_player_power)
	TextView pPower;
	@BindView(R.id.chb_battle_warrior)
	AppCompatCheckBox cb_warrior;
	@BindView(R.id.txt_battle_helper_power_value)
	TextView hPower;
	//Monster's views
	@BindView(R.id.txt_battle_m_lvl_value)
	TextView mLvl;
	@BindView(R.id.txt_battle_m_mods_value)
	TextView mMods;
	@BindView(R.id.txt_battle_m_power)
	TextView mPower;
	@BindView(R.id.txt_battle_m_tr_value)
	TextView mTreasures;
	@BindView(R.id.diagonal_divider)
	View mDivider;
	//Buttons
	@BindView(R.id.fab_battle_add_helper)
	FloatingActionButton helperBtn;

	private Tracker mTracker;
	private Unbinder unbinder;
	private Player player;
	private Player helper;
	private ArrayList<Player> players;
	private Monster monster;
	private int mMaxLvl;
	private int selectedIndex;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_battle_container);

		// Obtain the shared Tracker instance.
		MyApplication application = (MyApplication) getApplication();
		mTracker = application.getDefaultTracker();
		mTracker.send(new HitBuilders.EventBuilder()
				.setAction("BattleScreen")
				.setCategory("Screen")
				.setLabel("BattleActivity")
				.build());

		if (savedInstanceState == null) {

			final Intent intent = getIntent();
			final Bundle bundle = intent.getExtras();
			int selectedPlayerId = bundle.getInt(GameActivity.PLAYER, 0);
			players = bundle.getParcelableArrayList(GameActivity.PLAYERS_KEY);
			if (players == null || players.isEmpty()) {
				setResult(RESULT_CANCELED);
				finish();
			}

			player = players.remove(selectedPlayerId);    //remove so it won't be shown in help list
			mMaxLvl = intent.getIntExtra(GameActivity.MAX_LVL, 10);

		} else {
			players = savedInstanceState.getParcelableArrayList(PLAYERS_KEY);
			player = savedInstanceState.getParcelable(PLAYER);
		}

		monster = new Monster();

		//Binding views
		unbinder = ButterKnife.bind(this);

		initDivider();

		fillViewValues();
		final ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setTitle(player.getName() + " VS " + "Monster");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(PLAYERS_KEY, players);
		outState.putParcelable(PLAYER, player);
	}

	@OnClick({R.id.fab_battle_lvl_up, R.id.fab_battle_lvl_dwn,
			R.id.fab_battle_gear_up, R.id.fab_battle_gear_dwn,
			R.id.fab_battle_mods_up, R.id.fab_battle_mods_dwn})
	public void playerClicks(FloatingActionButton fab) {
		int id = fab.getId();
		switch (id) {
			case R.id.fab_battle_lvl_up:
				if (player.getLevel() + 1 == mMaxLvl) {
					Toast.makeText(this, "You cannot win in game before finishing the battle", Toast.LENGTH_SHORT).show();
				} else {
					player.setLevel(player.getLevel() + 1);
				}
				break;
			case R.id.fab_battle_lvl_dwn:
				if (player.getLevel() != 1) {
					player.setLevel(player.getLevel() - 1);
				}
				break;
			case R.id.fab_battle_gear_up:
				player.setGear(player.getGear() + 1);
				break;
			case R.id.fab_battle_gear_dwn:
				player.setGear(player.getGear() - 1);
				break;
			case R.id.fab_battle_mods_up:
				player.setMods(player.getMods() + 1);
				break;
			case R.id.fab_battle_mods_dwn:
				player.setMods(player.getMods() - 1);
				break;
			default:
				break;
		}
		updateViewValues();
	}

	@OnClick({R.id.fab_battle_m_lvl_up, R.id.fab_battle_m_lvl_dwn,
			R.id.fab_battle_m_mods_up, R.id.fab_battle_m_mods_dwn,
			R.id.fab_battle_m_tr_up, R.id.fab_battle_m_tr_dwn})
	public void monsterClicks(FloatingActionButton fab) {
		int id = fab.getId();
		switch (id) {
			case R.id.fab_battle_m_lvl_up:
				monster.setLevel(monster.getLevel() + 1);
				break;
			case R.id.fab_battle_m_lvl_dwn:
				if (monster.getLevel() != 1) {
					monster.setLevel(monster.getLevel() - 1);
				}
				break;
			case R.id.fab_battle_m_mods_up:
				monster.setMods(monster.getMods() + 1);
				break;
			case R.id.fab_battle_m_mods_dwn:
				monster.setMods(monster.getMods() - 1);
				break;
			case R.id.fab_battle_m_tr_up:
				monster.setTreasures(monster.getTreasures() + 1);
				break;
			case R.id.fab_battle_m_tr_dwn:
				if (monster.getTreasures() != 0) {
					monster.setTreasures(monster.getTreasures() - 1);
				}
				break;
			default:
				break;
		}
		updateViewValues();
	}

	@OnClick(R.id.fab_battle_add_helper)
	public void selectHelper() {
		if (!players.isEmpty()) {
			final List<String> playersList = new ArrayList<>(players.size());
			for (Player player : players) {
				playersList.add(player.getHelperInfo());
			}

			MaterialDialog dialog = new MaterialDialog.Builder(this)
					.items(playersList)
					.itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
						@Override
						public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
							return true;
						}
					})
					.positiveText(R.string.title_dialog_choose_helper_ok)
					.onPositive(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							helperSelected(dialog.getSelectedIndex());
						}
					})
					.neutralText(R.string.title_dialog_choose_helper_cancel)
					.onNeutral(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							dialog.dismiss();
						}
					})
					.negativeText(R.string.title_dialog_choose_helper_remove)
					.onNegative(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							helperSelected(-1);
						}
					})
					.backgroundColor(getResources().getColor(R.color.background))
					.dividerColor(getResources().getColor(R.color.text_color))
					.autoDismiss(true)
					.build();
			dialog.show();
		}
	}

	private void helperSelected(int position) {
		if (position == -1) {
			helper = null;
		} else {
			helper = players.get(position);
		}
		selectedIndex = position;
		updateViewValues();
	}

	@OnClick(R.id.fab_battle_fight)
	public void fight() {
		int pPowerValue = Integer.parseInt(pPower.getText().toString());
		int mPowerValue = Integer.parseInt(mPower.getText().toString());
		boolean willWin = pPowerValue > mPowerValue;

		if (willWin) {
			new MaterialDialog.Builder(this)
					.title(R.string.battle_dialog_title_win)
					.content(R.string.battle_dialog_msg)
					.titleColor(getResources().getColor(R.color.text_color))
					.contentColor(getResources().getColor(R.color.text_color))
					.positiveText(R.string.ok_btn_dialog_battle_continue)
					.onPositive(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							battleFightResult(BATTLE_RESULT_OK);
							dialog.dismiss();
						}
					})
					.negativeText(R.string.cancel_btn_for_dialog_battle)
					.onNegative(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							dialog.dismiss();
						}
					})
					.backgroundColor(getResources().getColor(R.color.background))
					.show();
		} else {
			new MaterialDialog.Builder(this)
					.title(R.string.battle_dialog_title_lose)
					.content(R.string.battle_dialog_msg_lose)
					.titleColor(getResources().getColor(R.color.text_color))
					.contentColor(getResources().getColor(R.color.text_color))
					.positiveText(R.string.battle_dialog_lose_run_away)
					.onPositive(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							dialog.dismiss();
							runAway();
						}
					})
					.negativeText(R.string.battle_dialog_lose_anyway)
					.onNegative(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							battleFightResult(BATTLE_RESULT_FAIL);
							dialog.dismiss();
						}
					})
					.backgroundColor(getResources().getColor(R.color.background))
					.show();
		}
	}

	private void battleFightResult(int battleResult) {
		setResult(battleResult);
		finishBattle();
	}

	@OnClick(R.id.fab_battle_run_away)
	public void runAway() {
		new MaterialDialog.Builder(this)
				.title(R.string.battle_dialog_title_run)
				.content(R.string.battle_dialog_msg_run)
				.titleColor(getResources().getColor(R.color.text_color))
				.contentColor(getResources().getColor(R.color.text_color))
				.positiveText(R.string.battle_dialog_run_confirm)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						dialog.dismiss();
						showRunDice();
					}
				})
				.negativeText(R.string.battle_dialog_run_cancel)
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						dialog.dismiss();
					}
				})
				.backgroundColor(getResources().getColor(R.color.background))
				.autoDismiss(true)
				.show();
	}

	@OnCheckedChanged(R.id.chb_battle_warrior)
	public void isWarrior() {
		updateViewValues();
	}

	private void showRunDice() {

		mTracker.send(new HitBuilders.EventBuilder()
				.setAction("DiceRolled")
				.setLabel("BattleActivity")
				.build());
		int Min = 1;
		int Max = 6;
		final int dice = Min + (int) (Math.random() * ((Max - Min) + 1));

		MaterialDialog diceDialog = new MaterialDialog.Builder(this)
				.title(R.string.dice)
				.titleColor(getResources().getColor(R.color.text_color))
				.customView(R.layout.dice_dialog, false)
				.backgroundColor(getResources().getColor(R.color.background))
				.autoDismiss(true)
				.positiveText(R.string.dialog_ok_btn)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						runAwayResult(dice);
					}
				})
				.build();


		final ImageView imageView = (ImageView) diceDialog.getCustomView().findViewById(R.id.imgDice);

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
		final TextView textView = (TextView) diceDialog.getCustomView().findViewById(R.id.run_away_result);
		textView.setVisibility(View.VISIBLE);
		textView.setText(SUCCESS_RUN_AWAY <= dice ? R.string.run_away_success : R.string.run_away_fail);
		textView.setTextColor(getResources().getColor(R.color.text_color));

		diceDialog.show();
	}

	private void runAwayResult(int dice) {
		if (SUCCESS_RUN_AWAY <= dice) {
			setResult(RUN_AWAY_RESULT_OK);
		} else {
			setResult(RUN_AWAY_RESULT_FAIL);
		}
		finishBattle();
	}

	public void finishBattle() {
		finish();
	}

	private void updateViewValues() {
		fillViewValues();
	}

	private void fillViewValues() {
		//Filling player's values
		pName.setText(player.getName());
		pLvl.setText(String.valueOf(player.getLevel()));
		pGear.setText(String.valueOf(player.getGear()));
		pMods.setText(String.valueOf(player.getMods()));
		int pPowerValue = player.getPower() + player.getMods();

		if (helper != null) {
			hPower.setText("+" + helper.getPower());
			pPowerValue = pPowerValue + helper.getPower();
		} else {
			hPower.setText("");
		}
		if (cb_warrior.isChecked()) {
			pPowerValue++;
		}

		pPower.setText(String.valueOf(pPowerValue));

		//Filling monster's values
		mLvl.setText(String.valueOf(monster.getLevel()));
		mMods.setText(String.valueOf(monster.getMods()));
		mPower.setText(String.valueOf(monster.getPower()));
		mTreasures.setText(String.valueOf(monster.getTreasures()));

		if (players.isEmpty()) { //after removing currentPlayer
			helperBtn.setEnabled(false);
		} else {
			helperBtn.setEnabled(true);
		}
		updateHelperBtn();
	}

	private void updateHelperBtn() {
		final Drawable icon;
		int resId = R.drawable.ic_person_add_24dp;
		if (helper != null) {
			resId = R.drawable.ic_autorenew_helper_24dp;
		}

		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			icon = VectorDrawableCompat.create(this.getResources(), resId, this.getTheme());
		} else {
			icon = this.getResources().getDrawable(resId, this.getTheme());
		}
		helperBtn.setImageDrawable(icon);
	}

	@Override
	public boolean onNavigateUp() {
		showCancelDialog();
		return false;
	}

	@Override
	public void onBackPressed() {
		showCancelDialog();
	}

	private void showCancelDialog() {
		new MaterialDialog.Builder(this)
				.title(R.string.title_dialog_confirm)
				.titleColor(getResources().getColor(R.color.text_color))
				.contentColor(getResources().getColor(R.color.text_color))
				.content(R.string.message_for_dialog_battle_leave)
				.positiveText(R.string.ok_btn_dialog_battle_leave)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						leaveBattle();
					}
				})
				.negativeText(R.string.cancel_btn_dialog_battle_leave)
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						dialog.dismiss();
					}
				})
				.backgroundColor(getResources().getColor(R.color.background))
				.autoDismiss(true)
				.show();
	}

	private void leaveBattle() {
		setResult(BATTLE_RESULT_CANCEL);
		finishBattle();
	}

	@Override
	protected void onDestroy() {
		unbinder.unbind();
		super.onDestroy();
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	private void initDivider() {
		Drawable divider;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			divider = VectorDrawableCompat.create(getResources(), R.drawable.ic_test_divider_1, getTheme());
		} else {
			divider = getResources().getDrawable(R.drawable.ic_test_divider_1, getTheme());
		}
		mDivider.setBackgroundDrawable(divider);
	}
}
