package com.g_art.munchkinlevelcounter.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;
import com.g_art.munchkinlevelcounter.analytics.AnalyticsActions;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.model.Sex;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by agulia on 2/23/17.
 */

public class PlayerDetailsScreen extends AppCompatActivity implements ColorChooserDialog.ColorCallback {
	public static final String PLAYER_KEY = "player";
	private static final String SINGLE_MODE_KEY = "singleMode";
	@BindView(R.id.single_player_name)
	EditText mEdPlayerName;
	@BindView(R.id.single_player_color)
	View playerColor;
	@BindView(R.id.imb_single_gender)
	ImageView playerGender;
	private Unbinder unbinder;
	private Player playerForTheGame;

	private boolean singleMode = true;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_screen_activity);
		unbinder = ButterKnife.bind(this);


		if (savedInstanceState != null && savedInstanceState.containsKey(PLAYER_KEY)) {
			playerForTheGame = savedInstanceState.getParcelable(PLAYER_KEY);
			singleMode = savedInstanceState.getBoolean(SINGLE_MODE_KEY);
		} else {
			final Intent requestIntent = getIntent();
			if (requestIntent == null) {
				singleMode = true;
			} else {
				if (requestIntent.getIntExtra(GameActivity.REQUEST_CODE, 0) == GameActivity.REQUEST_ADD_PLAYER) {
					singleMode = false;
				} else {
					singleMode = true;
				}
			}

		}

		if (playerForTheGame == null) {
			playerForTheGame = new Player("");
		}

		mEdPlayerName.setFocusableInTouchMode(true);
		playerColor.setFocusable(true);
		bindDataToViews();

		updateTitle();

	}

	private void updateTitle() {
		final ActionBar bar = getSupportActionBar();
		if (bar != null && !singleMode) {
			bar.setTitle(R.string.add_player);
		}
	}

	private void bindDataToViews() {
		setPlayerName();
		setPlayerGender();
		setPlayerColor();
	}

	private void setPlayerGender() {
		if (playerForTheGame.getSex() == Sex.MAN) {
			playerGender.setImageResource(R.drawable.ic_gender_man);
		} else {
			playerGender.setImageResource(R.drawable.ic_gender_woman);
		}
	}

	private void setPlayerName() {
		mEdPlayerName.setText(playerForTheGame.getName());
		final int position = mEdPlayerName.length();
		mEdPlayerName.setSelection(position);
	}

	private void setPlayerColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			final ShapeDrawable icon = new ShapeDrawable(new OvalShape());
			icon.setIntrinsicHeight(24);
			icon.setIntrinsicWidth(24);
			icon.getPaint().setColor(playerForTheGame.getColor());
			playerColor.setBackground(icon);
		} else {
			playerColor.setBackgroundColor(playerForTheGame.getColor());
		}
	}

	@OnClick(R.id.single_player_color)
	public void changeColor() {
		new ColorChooserDialog.Builder(this, R.string.choose_player_color)
				.allowUserColorInputAlpha(false)
				.allowUserColorInput(false)
				.cancelButton(R.string.dialog_cancel_btn)
				.backButton(R.string.dialog_back_btn)
				.doneButton(R.string.dialog_ok_btn)
				.preselect(playerForTheGame.getColor())
				.dynamicButtonColor(true)
				.show();
	}

	@OnClick(R.id.imb_single_gender)
	public void changeGender() {
		if (Sex.MAN == playerForTheGame.getSex()) {
			playerForTheGame.setSex(Sex.WOMAN);
		} else {
			playerForTheGame.setSex(Sex.MAN);
		}
		setPlayerGender();
	}

	@OnClick(R.id.fab_start_game)
	public void startTheGame() {
		if (collectAllPlayerInfo()) {
			if (singleMode) {
				startTheGameWithPlayer();
			} else {
				addPlayerToGame();
			}
		}
	}

	private void addPlayerToGame() {
		final Intent playerIntent = new Intent();
		playerIntent.putExtra(GameActivity.PLAYER, playerForTheGame);
		setResult(GameActivity.ADD_PLAYER_RESULT_OK, playerIntent);
		finish();
	}

	private boolean collectAllPlayerInfo() {
		if (checkPlayerName()) {
			playerForTheGame.setName(mEdPlayerName.getText().toString());
			return true;
		} else {
			return false;
		}
	}

	private boolean checkPlayerName() {
		final String pName = mEdPlayerName.getText().toString();
		if (pName.isEmpty() || pName.equals(" ")) {
			Toast.makeText(this, getResources().getString(R.string.name_cannot_be_empty), Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void startTheGameWithPlayer() {
		final Intent intent = new Intent(this, GameActivity.class);
		final ArrayList<Player> players = new ArrayList<>();
		players.add(playerForTheGame);
		intent.putParcelableArrayListExtra(GameActivity.PLAYERS_LIST_KEY, players);

		Analytics.getInstance().logEvent(AnalyticsActions.Game_Starting, PlayerDetailsScreen.class.toString());
		startActivity(intent);
	}

	@OnTextChanged(value = R.id.single_player_name,
			callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
	public void changeName(Editable editable) {
		playerForTheGame.setName(editable.toString());
	}

	@Override
	public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
		playerForTheGame.setColor(selectedColor);
		setPlayerColor();
	}

	@Override
	public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {
		dialog.dismiss();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(PLAYER_KEY, playerForTheGame);
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
}
