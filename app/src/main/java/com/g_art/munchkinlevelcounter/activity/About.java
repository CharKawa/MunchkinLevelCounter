package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by G_Art on 12/9/2014.
 */
public class About extends Activity implements View.OnClickListener {

    // Debug tag, for logging
    static final String TAG = "AboutActivity";

    private Button btnRate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnRate = (Button) findViewById(R.id.btn_Rate);
        btnRate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Rate:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Try Amazon market
                intent.setData(Uri.parse("amzn://apps/android?p=com.g_art.munchkinlevelcounter"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                
                if (!MyStartActivity(intent)) {
                    //Market (Amazon market) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=com.g_art.munchkinlevelcounter"));
                    if (!MyStartActivity(intent)) {
                        //Well if this also fails, we have run out of options, inform the user.
                        Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private boolean MyStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
    
}
