package com.g_art.munchkinlevelcounter.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by G_Art on 1/8/2014.
 */
public class Settings extends AppCompatActivity implements View.OnClickListener {

	private EditText edTextMaxLvl;
	private SettingsHandler setHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		setHandler = SettingsHandler.getInstance(mPrefs);

		edTextMaxLvl = (EditText) findViewById(R.id.edTextMaxLvl);
		if (setHandler.loadSettings()) {
			edTextMaxLvl.setText(Integer.toString(setHandler.getMaxLvl()), TextView.BufferType.EDITABLE);
		}

		edTextMaxLvl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.edTextMaxLvl:
				try {
					saveSettings(parseLevel());
					break;
				} catch (NumberFormatException ex) {
					Toast.makeText(this,
							getString(R.string.error_max_lvl_settings),
							Toast.LENGTH_SHORT
					).show();
					edTextMaxLvl.setText(Integer.toString(setHandler.getMaxLvl()), TextView.BufferType.EDITABLE);
				}

		}
	}

	private int parseLevel() throws NumberFormatException {
		int mLvl = Integer.parseInt(edTextMaxLvl.getText().toString());
		if (mLvl < SettingsHandler.MIN_LVL) {
			Toast.makeText(this,
					getString(R.string.error_max_level_one),
					Toast.LENGTH_SHORT
			).show();
			mLvl = setHandler.getMaxLvl();
			edTextMaxLvl.setText(mLvl, TextView.BufferType.EDITABLE);
		}
		return mLvl;
	}

	private void saveSettings(int maxLvl) {
		setHandler.saveSettings(maxLvl);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
}
