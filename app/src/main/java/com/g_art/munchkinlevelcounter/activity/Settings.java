package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.settings.SettingsHandler;

/**
 * Created by G_Art on 1/8/2014.
 */
public class Settings extends Activity implements CompoundButton.OnCheckedChangeListener {

    private Switch statsSwitch;
    private SharedPreferences mPrefs;
    private SettingsHandler setHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setHandler = SettingsHandler.getInstance(mPrefs);

        statsSwitch = (Switch) findViewById(R.id.switchStats);
        statsSwitch.setChecked(setHandler.loadSettings());

        statsSwitch.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            setHandler.saveSettings(true);
        } else {
            setHandler.saveSettings(false);
        }
    }
}
