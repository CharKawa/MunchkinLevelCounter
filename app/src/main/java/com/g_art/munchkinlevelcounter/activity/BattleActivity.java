package com.g_art.munchkinlevelcounter.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.fragments.dialog.ConfirmDialog;
import com.g_art.munchkinlevelcounter.fragments.dialog.HelperListDialog;
import com.g_art.munchkinlevelcounter.model.Monster;
import com.g_art.munchkinlevelcounter.model.Player;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

/**
 * MunchkinLevelCounter
 * Created by fftem on 03-Aug-16.
 */

public class BattleActivity extends AppCompatActivity implements ConfirmDialog.DialogClickEvents {

	public static String HELPER_LIST = "helpers";

	private Tracker mTracker;
	private Unbinder unbinder;

	@BindBool (R.bool.portrait_only)
	boolean mPortrait;

	private Player player;
	private Player helper;
	private List<Player> players;
	private Monster monster;
	private int mMaxLvl;

	//Player's views
	@BindView (R.id.txt_battle_player_name)
	TextView pName;
	@BindView (R.id.txt_battle_lvl_value)
	TextView pLvl;
	@BindView (R.id.txt_battle_gear_value)
	TextView pGear;
	@BindView (R.id.txt_battle_mods_value)
	TextView pMods;
	@BindView (R.id.txt_battle_player_power)
	TextView pPower;

	//Monster's views
	@BindView (R.id.txt_battle_m_lvl_value)
	TextView mLvl;
	@BindView (R.id.txt_battle_m_mods_value)
	TextView mMods;
	@BindView (R.id.txt_battle_m_power)
	TextView mPower;
	@BindView (R.id.diagonal_divider)
	View mDivider;

	//Buttons
	@BindView(R.id.fab_battle_add_helper)
	FloatingActionButton helperBtn;
	//CheckBox
	@BindView(R.id.chb_battle_warrior)
	AppCompatCheckBox checkBox;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mPortrait) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		setContentView(R.layout.activity_battle_container);

		// Obtain the shared Tracker instance.
		MyApplication application = (MyApplication) getApplication();
		mTracker = application.getDefaultTracker();
		mTracker.send(new HitBuilders.EventBuilder()
				.setAction("BattleScreen")
				.setCategory("Screen")
				.setLabel("BattleActivity")
				.build());

		final Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();

		int selectedPlayerId = bundle.getInt(GameActivity.PLAYER, 0);

		players = bundle.getParcelableArrayList(GameActivity.PLAYERS_KEY);

		if (players == null || players.isEmpty()) {
			setResult(RESULT_CANCELED);
			finish();
		}

		player = players.get(selectedPlayerId);
		players.remove(selectedPlayerId);//remove so it won't be shown in help list

		monster = new Monster();
		mMaxLvl = intent.getIntExtra(GameActivity.MAX_LVL, 10);

		//Binding views
		unbinder = ButterKnife.bind(this);

		initDivider();

		fillViewValues();
		final ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setTitle(player.getName()+ " VS " + "Monster");
		}
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

	@OnClick ({R.id.fab_battle_lvl_up, R.id.fab_battle_lvl_dwn,
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

	@OnClick ({R.id.fab_battle_m_lvl_up, R.id.fab_battle_m_lvl_dwn,
			R.id.fab_battle_m_mods_up, R.id.fab_battle_m_mods_dwn})
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
			default:
				break;
		}
		updateViewValues();
	}

	@OnClick (R.id.fab_battle_add_helper)
	public void selectHelper() {
		Toast.makeText(this, "fab_battle_add_helper", Toast.LENGTH_SHORT).show();
		if (!players.isEmpty()) {
			final HelperListDialog helperListDialog = new HelperListDialog();
			final Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(HELPER_LIST, new ArrayList<Parcelable>(players));
			helperListDialog.setArguments(bundle);
			helperListDialog.show(getFragmentManager(), "helpersDialog");
		}
	}

	@OnClick (R.id.fab_battle_fight)
	public void fight() {
		int pPowerValue = Integer.parseInt(pPower.getText().toString());
		int mPowerValue = Integer.parseInt(mPower.getText().toString());
		boolean willWin = pPowerValue > mPowerValue;

		if (willWin) {
			Bundle bundle = new Bundle();
			bundle.putString(ConfirmDialog.SOURCE_KEY, "BattleActivity");
			bundle.putInt(ConfirmDialog.TITLE_KEY, R.string.battle_dialog_title_win);
			bundle.putInt(ConfirmDialog.MSG_KEY, R.string.battle_dialog_msg);
			bundle.putInt(ConfirmDialog.OK_KEY, R.string.ok_btn_dialog_battle_continue);
			bundle.putInt(ConfirmDialog.NOT_KEY, R.string.cancel_btn_for_dialog_battle);
			DialogFragment confirmDialog = new ConfirmDialog();
			confirmDialog.setArguments(bundle);
			confirmDialog.show(getFragmentManager(), "confirmDialog");
		} else {
			Bundle bundle = new Bundle();
			bundle.putString(ConfirmDialog.SOURCE_KEY, "BattleActivity");
			bundle.putInt(ConfirmDialog.TITLE_KEY, R.string.battle_dialog_title_lose);
			bundle.putInt(ConfirmDialog.MSG_KEY, R.string.battle_dialog_msg_lose);
			bundle.putInt(ConfirmDialog.OK_KEY, R.string.battle_dialog_lose_run_away);
			bundle.putInt(ConfirmDialog.NOT_KEY, R.string.battle_dialog_lose_anyway);
			DialogFragment confirmDialog = new ConfirmDialog();
			confirmDialog.setArguments(bundle);
			confirmDialog.show(getFragmentManager(), "confirmDialog");
		}
	}

	@OnClick (R.id.fab_battle_run_away)
	public void runAway() {
		Toast.makeText(this, "fab_battle_run_away", Toast.LENGTH_SHORT).show();

		Bundle bundle = new Bundle();
		bundle.putString(ConfirmDialog.SOURCE_KEY, "BattleActivity");
		bundle.putInt(ConfirmDialog.TITLE_KEY, R.string.battle_dialog_title_run);
		bundle.putInt(ConfirmDialog.MSG_KEY, R.string.battle_dialog_msg_run);
		bundle.putInt(ConfirmDialog.OK_KEY, R.string.battle_dialog_run_confirm);
		bundle.putInt(ConfirmDialog.NOT_KEY, R.string.battle_dialog_run_cancel);
		DialogFragment confirmDialog = new ConfirmDialog();
		confirmDialog.setArguments(bundle);
		confirmDialog.show(getFragmentManager(), "confirmDialog");
	}


	@Override
	public void positiveDialogClick(Bundle bundle) {
		if ("battle_fight".equalsIgnoreCase(bundle.getString(ConfirmDialog.REQUEST_KEY)));
	}

	@Override
	public void negativeDialogClick(Bundle bundle) {
		if ("battle_fight".equalsIgnoreCase(bundle.getString(ConfirmDialog.REQUEST_KEY)));
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
		int pPowerValue = player.getLevel() + player.getGear() + player.getMods();
		pPower.setText(String.valueOf(pPowerValue));

		//Filling monster's values
		mLvl.setText(String.valueOf(monster.getLevel()));
		mMods.setText(String.valueOf(monster.getMods()));
		int mPowerValue = monster.getLevel() + monster.getMods();
		mPower.setText(String.valueOf(mPowerValue));

		if (players.isEmpty()) { //after removing currentPlayer
			helperBtn.setEnabled(false);
		}
	}

	@Override
	protected void onDestroy() {
		unbinder.unbind();
		super.onDestroy();
	}
}
