package com.g_art.munchkinlevelcounter.fragments.stats;

import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.Stats;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;
import com.g_art.munchkinlevelcounter.model.Player;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

/**
 * Created by agulia on 1/19/17.
 */

public abstract class StatsFragment extends Fragment {

	protected void initView(View v, int statsType) {
		boolean isDataPresent = false;
		ArrayList<Player> playersList = null;

		if (getArguments() != null) {
			playersList = getArguments().getParcelableArrayList(Stats.PLAYER_KEY);
			isDataPresent = playersList != null && !playersList.isEmpty();
		}

		if (isDataPresent) {
			final LineChart mChart = (LineChart) v.findViewById(R.id.game_stats);
			mChart.setDrawGridBackground(false);
			// enable touch gestures
			mChart.setTouchEnabled(true);
			mChart.setScaleEnabled(true);
			// if disabled, scaling can be done on x- and y-axis separately
			mChart.setPinchZoom(true);

			// no description text
			mChart.getDescription().setEnabled(false);
			mChart.getLegend().setWordWrapEnabled(true);
			mChart.getLegend().setTextColor(getResources().getColor(R.color.text_color));
			mChart.getLegend().setTextSize(15f);
			mChart.getLegend().setXEntrySpace(15f);
			mChart.setBorderColor(getResources().getColor(R.color.text_color));

			final SparseArray<String> playersColors = StatsHandler.getStats(playersList, mChart, getActivity(), statsType);
			if (playersColors != null) {
				mChart.invalidate();
			}
		}
	}
}
