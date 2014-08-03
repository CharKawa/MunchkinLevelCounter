package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by G_Art on 1/8/2014.
 */
public class Settings extends Activity {
    private Spinner spLang;
    private ToggleButton tBtnStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spLang = (Spinner) findViewById(R.id.spLang);
        tBtnStats = (ToggleButton) findViewById(R.id.tBtnStats);
    }
}
