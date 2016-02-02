package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.fragments.info.ThanksPopup;

/**
 * MunchinLevelCounter
 * Created by G_Art on 2/2/2016.
 */
public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            ThanksPopup newFragment = new ThanksPopup();

            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment).commit();
        }
    }
}
