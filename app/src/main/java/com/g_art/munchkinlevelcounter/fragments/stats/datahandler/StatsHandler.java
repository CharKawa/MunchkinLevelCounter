package com.g_art.munchkinlevelcounter.fragments.stats.datahandler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;

import com.db.chart.Tools;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by G_Art on 3/9/2014.
 */
public class StatsHandler {
	public final static int LVL_STATS = 0;
	public final static int GEAR_STATS = 1;
	public final static int POWER_STATS = 2;
	private final static int CLEAR_STRING_BUILDER = 0;

	@SuppressWarnings("unchecked")
	public static SparseArray<String> getStats(ArrayList<Player> playersList, LineChartView chartView, final Context context, int stats) {
		int MAX_VALUE = 0;
		int MIN_VALUE = 0;
		Random rnd = new Random();
		StringBuilder s = new StringBuilder();
		SparseArray<String> playersColorSparse = new SparseArray<>();

		LineSet lineSet;
		ArrayList<ChartSet> statLines = new ArrayList<>();

		String lvl = context.getString(R.string.level);
		String gear = context.getString(R.string.gear);
		String power = context.getString(R.string.power_tab);


		if (playersList != null && chartView != null) {
			Collections.sort(playersList);
			for (Player player : playersList) {

				s.append(player.getName());
				s.append(" - ");
				s.append(context.getString((player.isWinner() ? R.string.winner : R.string.loser)));

				lineSet = new LineSet();

				switch (stats) {
					case LVL_STATS:
						MIN_VALUE = 1;
						for (int i = 0; i < player.getLvlStats().size(); i++) {

							int lvlValue = Integer.parseInt(player.getLvlStats().get(i));
							lineSet.addPoint(new Point(String.valueOf(" "), lvlValue));

							if (MAX_VALUE < lvlValue) {
								MAX_VALUE = lvlValue;
							}
						}
						s.append(" ").append(lvl).append(": ").append(player.getLevel());
						break;
					case GEAR_STATS:
						boolean gearChanged = false;
						MIN_VALUE = 0;
						for (int i = 0; i < player.getGearStats().size(); i++) {
							int gearValue = Integer.parseInt(player.getGearStats().get(i));
							lineSet.addPoint(new Point(String.valueOf(" "), gearValue));

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
						s.append(" ").append(gear).append(": ").append(player.getGear());
						break;
					case POWER_STATS:
						MIN_VALUE = 0;
						for (int i = 0; i < player.getPowerStats().size(); i++) {
							int powerValue = Integer.parseInt(player.getPowerStats().get(i));
							lineSet.addPoint(new Point(String.valueOf(" "), powerValue));

							if (MAX_VALUE < powerValue) {
								MAX_VALUE = powerValue;
							}
							if (MIN_VALUE > powerValue) {
								MIN_VALUE = powerValue;
							}
						}
						s.append(" ").append(power).append(": ").append(player.getLevel() + player.getGear());
						break;
				}

				int color = player.getColor();
				if (color == 0) {
					player.setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
					color = player.getColor();
				}
				lineSet.setColor(color);


				playersColorSparse.put(color, s.toString());
				s.setLength(CLEAR_STRING_BUILDER);

				lineSet.setSmooth(true);

				statLines.add(lineSet);
			}

			Paint mLineGridPaint = new Paint();
			mLineGridPaint.setColor(context.getResources().getColor(R.color.text_color));
			mLineGridPaint.setAntiAlias(true);
			mLineGridPaint.setStrokeWidth(Tools.fromDpToPx(.10f));

			chartView.setXAxis(true).setYAxis(true).setGrid(LineChartView.GridType.FULL, mLineGridPaint);
			int STEP = 1;
			chartView.setAxisBorderValues(MIN_VALUE, MAX_VALUE + 1, STEP);

			chartView.setFontSize(30);

			chartView.addData(statLines);

			return playersColorSparse;
		}

		return null;
	}
}
