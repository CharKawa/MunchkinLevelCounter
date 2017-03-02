package com.g_art.munchkinlevelcounter.fragments.stats.datahandler;

import android.content.Context;
import android.util.SparseArray;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by G_Art on 3/9/2014.
 */
public class StatsHandler {
	public final static int LVL_STATS = 0;
	public final static int GEAR_STATS = 1;
	public final static int POWER_STATS = 2;
	public static final float LINE_WIDTH = 3f;
	private final static int CLEAR_STRING_BUILDER = 0;

	@SuppressWarnings("unchecked")
	public static SparseArray<String> getStats(ArrayList<Player> playersList, LineChart chartView, final Context context, int stats) {
		int MAX_VALUE = 0;
		int MIN_VALUE = 0;
		final SparseArray<String> playersColorSparse = new SparseArray<>();

		LineDataSet set;
		ArrayList<ILineDataSet> dataSets = new ArrayList<>();

		List<Entry> dataSet;

		if (playersList != null && chartView != null) {
			Collections.sort(playersList);
			for (Player player : playersList) {
				dataSet = new ArrayList<>();

				switch (stats) {
					case LVL_STATS:
						MIN_VALUE = 1;
						for (int i = 0; i < player.getLvlStats().size(); i++) {
							int lvlValue = Integer.parseInt(player.getLvlStats().get(i));

							dataSet.add(new Entry(i, lvlValue));

							if (MAX_VALUE < lvlValue) {
								MAX_VALUE = lvlValue;
							}
						}
						break;
					case GEAR_STATS:
						boolean gearChanged = false;
						MIN_VALUE = 0;
						for (int i = 0; i < player.getGearStats().size(); i++) {
							int gearValue = Integer.parseInt(player.getGearStats().get(i));

							dataSet.add(new Entry(i, gearValue));

							if (MAX_VALUE < gearValue) {
								MAX_VALUE = gearValue;
							}
							if (gearValue != 0) {
								gearChanged = true;
							}
							if (MIN_VALUE > gearValue) {
								MIN_VALUE = gearValue;
							}
						}
						if (!gearChanged) {
							MAX_VALUE = GEAR_STATS;
						}
						break;
					case POWER_STATS:
						MIN_VALUE = 0;
						for (int i = 0; i < player.getPowerStats().size(); i++) {
							int powerValue = Integer.parseInt(player.getPowerStats().get(i));

							dataSet.add(new Entry(i, powerValue));

							if (MAX_VALUE < powerValue) {
								MAX_VALUE = powerValue;
							}
							if (MIN_VALUE > powerValue) {
								MIN_VALUE = powerValue;
							}
						}
						break;
				}

				int color = player.getColor();
				if (color == 0) {
					player.generateColor();
					color = player.getColor();
				}

				set = new LineDataSet(dataSet, player.getName() + " - " + context.getString((player.isWinner() ? R.string.winner : R.string.loser)));
				set.setDrawCircles(false);
				set.setColor(color);
				set.setLineWidth(LINE_WIDTH);
				set.setCubicIntensity(0.1f);
				set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
				set.setDrawValues(false);
				set.setHighlightEnabled(false);

				dataSets.add(set);
			}

			chartView.setData(new LineData(dataSets));

			return playersColorSparse;
		}

		return null;
	}

}
