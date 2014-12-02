package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by G_Art on 12/9/2014.
 */
public class About extends Activity implements View.OnClickListener {

    private Button btnRate;
    private Button btnDonate099;
    private Button btnDonate199;
    private Button btnDonate399;
    private Button btnDonate999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnRate = (Button) findViewById(R.id.btn_Rate);
        btnRate.setOnClickListener(this);

        btnDonate099 = (Button) findViewById(R.id.btn_donate_099);
        btnDonate099.setOnClickListener(this);

        btnDonate199 = (Button) findViewById(R.id.btn_donate_199);
        btnDonate199.setOnClickListener(this);

        btnDonate399 = (Button) findViewById(R.id.btn_donate_399);
        btnDonate399.setOnClickListener(this);

        btnDonate999 = (Button) findViewById(R.id.btn_donate_999);
        btnDonate999.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Rate:
                break;
            case R.id.btn_donate_099:
//                purchase(new Sku());
                break;
            case R.id.btn_donate_199:
                break;
            case R.id.btn_donate_399:
                break;
            case R.id.btn_donate_999:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
