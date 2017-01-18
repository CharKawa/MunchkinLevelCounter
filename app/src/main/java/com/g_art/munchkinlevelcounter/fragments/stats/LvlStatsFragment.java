package com.g_art.munchkinlevelcounter.fragments.stats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.Stats;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;
import com.g_art.munchkinlevelcounter.model.Player;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LvlStatsFragment extends Fragment {

	private LineChart mChart;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_game_stats, container, false);

		boolean isDataPresent = false;
		ArrayList<Player> playersList = null;
		final SparseArray<String> playersColors;

		if (getArguments() != null) {
			playersList = getArguments().getParcelableArrayList(Stats.PLAYER_KEY);
			isDataPresent = playersList != null && !playersList.isEmpty();
		}

		if (isDataPresent) {
//			LineChartView chartView = (LineChartView) v.findViewById(R.id.LineChart);

			mChart = (LineChart) v.findViewById(R.id.game_stats);
			mChart.setDrawGridBackground(false);
			// enable touch gestures
			mChart.setTouchEnabled(true);
			mChart.setScaleEnabled(true);
			// if disabled, scaling can be done on x- and y-axis separately
			mChart.setPinchZoom(true);

			// no description text
			mChart.getDescription().setEnabled(false);

//			mChart.saveToGallery("munchkin_game_stat", 100);

			playersColors = StatsHandler.getStats(playersList, mChart, getActivity(), StatsHandler.LVL_STATS);
			if (playersColors != null) {
//				final SparseStringsAdapter sparseStringsAdapter = new SparseStringsAdapter(getActivity(), playersColors);
//				final ListView inStatsPlayersList = (ListView) v.findViewById(R.id.isStatsPlayersList);
//				inStatsPlayersList.setAdapter(sparseStringsAdapter);
			}
		}

		return v;
	}
}
