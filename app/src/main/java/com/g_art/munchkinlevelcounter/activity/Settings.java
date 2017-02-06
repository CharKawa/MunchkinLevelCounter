package com.g_art.munchkinlevelcounter.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;

import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by G_Art on 1/8/2014.
 */
public class Settings extends AppCompatActivity {

	private TextInputEditText edTextMaxLvl;
	private TextInputLayout textInputLayout;
	private SettingsHandler setHandler;
	private String EMPTY_STRING = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		setHandler = SettingsHandler.getInstance(mPrefs);

		edTextMaxLvl = (TextInputEditText) findViewById(R.id.edTextMaxLvl);
		if (setHandler.loadSettings()) {
			edTextMaxLvl.setText(String.valueOf(setHandler.getMaxLvl()), TextView.BufferType.EDITABLE);
		}

		textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
		edTextMaxLvl.setOnEditorActionListener(ActionListener.newInstance(this));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (shouldShowError()) {
			saveSettings(setHandler.getMaxLvl());
		} else {
			String lvl = edTextMaxLvl.getText().toString();
			int mLvl = Integer.parseInt(lvl);
			saveSettings(mLvl);
		}
	}

	private void saveSettings(int maxLvl) {
		setHandler.saveSettings(maxLvl);
	}

	private boolean shouldShowError() {
		String lvl = edTextMaxLvl.getText().toString();
		if (lvl.equals(EMPTY_STRING) || lvl.equals(" ")) {
			return true;
		}
		int mLvl = Integer.parseInt(lvl);
		return mLvl < SettingsHandler.MIN_LVL;
	}

	private void showError() {
		textInputLayout.setError(getString(R.string.error_max_level_one));
	}

	private void hideError() {
		textInputLayout.setError(EMPTY_STRING);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	private static final class ActionListener implements TextView.OnEditorActionListener {
		private final WeakReference<Settings> settingsWeakReference;

		private ActionListener(WeakReference<Settings> settingsWeakReference) {
			this.settingsWeakReference = settingsWeakReference;
		}

		public static ActionListener newInstance(Settings mainActivity) {
			WeakReference<Settings> mainActivityWeakReference = new WeakReference<>(mainActivity);
			return new ActionListener(mainActivityWeakReference);
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			Settings settings = settingsWeakReference.get();
			if (settings != null) {
				if (actionId == EditorInfo.IME_ACTION_GO && settings.shouldShowError()) {
					settings.showError();
				} else {
					settings.hideError();
				}
			}
			return true;
		}
	}
}
