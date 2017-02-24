package com.g_art.munchkinlevelcounter.activity;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.model.Sex;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by agulia on 2/23/17.
 */

public class SingleModePrepareActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {
	public static final String PLAYER_KEY = "player";
	@BindView(R.id.single_player_name)
	EditText playerName;
	@BindView(R.id.single_player_color)
	View playerColor;
	@BindView(R.id.imb_single_gender)
	ImageView playerGender;
	private Unbinder unbinder;
	private Player playerForTheGame;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_screen_activity);
		unbinder = ButterKnife.bind(this);

		if (savedInstanceState != null && savedInstanceState.containsKey(PLAYER_KEY)) {
			playerForTheGame = savedInstanceState.getParcelable(PLAYER_KEY);
		}
		if (playerForTheGame == null) {
			playerForTheGame = new Player(getString(R.string.player_name));
		}

		playerName.setFocusableInTouchMode(true);
		playerColor.setFocusable(true);
		bindDataToViews();

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
		playerName.setText(playerForTheGame.getName());
		final int position = playerName.length();
		playerName.setSelection(position);
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
