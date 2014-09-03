package com.g_art.munchkinlevelcounter.activity.listeners;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by G_Art on 3/9/2014.
 */
public class StatsTabListener implements ActionBar.TabListener {

    private Fragment fr;

    public StatsTabListener(Fragment fragment) {
        fr = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.statsContainer, fr);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fr);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //nothing do here
    }
}
