package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;

/**
 * Created by G_Art on 1/8/2014.
 */
public class Settings extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Switch statsSwitch;
    private EditText edTextMaxLvl;
    private SharedPreferences mPrefs;
    private SettingsHandler setHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setHandler = SettingsHandler.getInstance(mPrefs);

        statsSwitch = (Switch) findViewById(R.id.switchStats);
        edTextMaxLvl = (EditText) findViewById(R.id.edTextMaxLvl);
        if (setHandler.loadSettings()) {
            statsSwitch.setChecked(setHandler.isStats());
            edTextMaxLvl.setText(Integer.toString(setHandler.getMaxLvl()), TextView.BufferType.EDITABLE);
        }

        statsSwitch.setOnCheckedChangeListener(this);
        edTextMaxLvl.setOnClickListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            saveSettings(isChecked, parseLevel());
            Toast.makeText(this,
                    getString(R.string.settings_saved),
                    Toast.LENGTH_SHORT
            ).show();
        } catch (NumberFormatException ex) {
            Toast.makeText(this,
                    getString(R.string.error_max_lvl_settings),
                    Toast.LENGTH_SHORT
            ).show();
            edTextMaxLvl.setText(Integer.toString(setHandler.getMaxLvl()), TextView.BufferType.EDITABLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edTextMaxLvl:
                try {
                    saveSettings(statsSwitch.isChecked(), parseLevel());
                    Toast.makeText(this,
                            getString(R.string.settings_saved),
                            Toast.LENGTH_SHORT
                    ).show();
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

    private void saveSettings(boolean isChecked, int maxLvl) {
        setHandler.saveSettings(isChecked, maxLvl);
    }
}
