package com.g_art.munchkinlevelcounter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.PreferenceScreen;

/**
 * Created by agulia on 2/7/17.
 */

public class MaxLevelDialogPreference extends DialogPreference {

	private View mView;

	public MaxLevelDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPersistent(false); //to indicate to the super Preference class that you persist the preference value on your own.
	}

	// update the view with the value(s) of your preference.
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		mView = view;

		final SharedPreferences sp = getSharedPreferences();
		final EditText ed = (EditText) mView.findViewById(R.id.maxLvL);
		ed.setText(String.valueOf(sp.getInt(PreferenceScreen.KEY_PREF_MAX_LEVEL, PreferenceScreen.DEFAULT_MAX_LVL)));
		final int position = ed.length();
		ed.setSelection(position);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult); // if positiveResult is true then persist the value(s) from your view to the SharedPreferences.
		if (positiveResult) {
			final SharedPreferences sp = getSharedPreferences();
			final EditText ed = (EditText) mView.findViewById(R.id.maxLvL);
			final String maxLvl = ed.getText().toString();
			int mLvl = Integer.parseInt(maxLvl);
			if (mLvl <= SettingsHandler.MIN_LVL) {
				Toast.makeText(getContext(),
						getContext().getString(R.string.error_max_level_one),
						Toast.LENGTH_SHORT
				).show();
			} else {
				sp.edit().putInt(PreferenceScreen.KEY_PREF_MAX_LEVEL, mLvl).apply();
			}
		}
	}
}
