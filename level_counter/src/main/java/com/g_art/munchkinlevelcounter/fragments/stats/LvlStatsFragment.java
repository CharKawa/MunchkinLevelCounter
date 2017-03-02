package com.g_art.munchkinlevelcounter.fragments.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LvlStatsFragment extends StatsFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_game_stats, container, false);

		initView(v, StatsHandler.LVL_STATS);

		return v;
	}
}
