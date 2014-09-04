package com.g_art.munchkinlevelcounter.activity.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.g_art.munchkinlevelcounter.fragments.stats.GearStatsFragment;
import com.g_art.munchkinlevelcounter.fragments.stats.LvlStatsFragment;
import com.g_art.munchkinlevelcounter.fragments.stats.PowerStatsFragment;

/**
 * Created by G_Art on 4/9/2014.
 */
public class StatsPageAdapter extends FragmentPagerAdapter {
    private Bundle bundle;

    public StatsPageAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // LvlStatsFragment activity
                LvlStatsFragment lvlFr = new LvlStatsFragment();
                lvlFr.setArguments(bundle);
                return lvlFr;
            case 1:
                // GearStatsFragment activity
                GearStatsFragment gearFr = new GearStatsFragment();
                gearFr.setArguments(bundle);
                return gearFr;
            case 2:
                // PowerStatsFragment activity
                PowerStatsFragment powerFr = new PowerStatsFragment();
                powerFr.setArguments(bundle);
                return powerFr;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
