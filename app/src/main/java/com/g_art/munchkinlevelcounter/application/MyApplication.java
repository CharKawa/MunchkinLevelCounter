package com.g_art.munchkinlevelcounter.application;

import android.app.Application;
import android.util.Log;

/**
 * MunchinLevelCounter
 * Created by G_Art on 29/10/2014.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TestMyApp", "Testing");
        initInstances();

    }

    protected void initInstances() {
        Log.d("TestMyApp", "Trying to init");

        Log.d("TestMyApp", "Init completed");
    }

}
