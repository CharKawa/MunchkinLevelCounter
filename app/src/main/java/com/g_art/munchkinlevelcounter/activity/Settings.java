package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by G_Art on 1/8/2014.
 */
public class Settings extends Activity {

    private Switch statsSwitch;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;
    private final String STATS_SETTINGS = "stats_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = getPreferences(Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        statsSwitch = (Switch) findViewById(R.id.switchStats);
        statsSwitch.setChecked(loadSettings());

        statsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    saveSettings(true);
                } else {
                    saveSettings(false);
                }
            }
        });
    }

    private boolean loadSettings() {
        if (!mPrefs.contains(STATS_SETTINGS)) {
            saveSettings(true);
        }
        boolean result = mPrefs.getBoolean(STATS_SETTINGS, true);
        return result;
    }

    private boolean saveSettings(boolean checked) {
        prefsEditor.putBoolean(STATS_SETTINGS, checked);
        boolean result = prefsEditor.commit();
        return result;
    }


}
