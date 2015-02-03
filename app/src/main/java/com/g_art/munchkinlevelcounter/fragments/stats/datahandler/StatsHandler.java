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
import com.g_art.munchkinlevelcounter.bean.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by G_Art on 3/9/2014.
 */
public class StatsHandler {
    private final static int CLEAR_STRING_BULDER = 0;
    private ArrayList<Player> playersList;

    private LineChartView chartView;

    private static final Random rnd = new Random();
    private SparseArray<String> playersColorSparse;
    private StringBuilder s;
    public final static int LVL_STATS = 0;
    public final static int GEAR_STATS = 1;
    public final static int POWER_STATS = 2;
    private int MIN_VALUE = 0;
    private int MAX_VALUE;
    private final int STEP = 1;


    public StatsHandler(ArrayList<Player> playersList, LineChartView chartView) {
        this.playersList = playersList;
        this.chartView = chartView;
        s = new StringBuilder();
    }

    public LineChartView getStats(final Context context, int stats) {

        playersColorSparse = new SparseArray<String>();

        LineSet lineSet;
        ArrayList<ChartSet> statLines = new ArrayList<ChartSet>();

        String lvl = context.getString(R.string.level);
        String gear = context.getString(R.string.gear);
        String power = context.getString(R.string.power_tab);

        if (playersList != null) {
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
                        }
                        if (!gearChanged) {
                            MAX_VALUE = GEAR_STATS;
                        }
                        s.append(" ").append(gear).append(": ").append(player.getGear());
                        break;
                    case POWER_STATS:
                        MIN_VALUE = 1;
                        for (int i = 0; i < player.getPowerStats().size(); i++) {
                            int powerValue = Integer.parseInt(player.getPowerStats().get(i));
                            lineSet.addPoint(new Point(String.valueOf(" "), powerValue));

                            if (MAX_VALUE < powerValue) {
                                MAX_VALUE = powerValue;
                            }
                        }
                        s.append(" ").append(power).append(": ").append(player.getLevel() + player.getGear());
                        break;
                }

                // Style dots
                lineSet.setDots(false);

                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                lineSet.setLineColor(color);


                playersColorSparse.put(color, s.toString());
                s.setLength(CLEAR_STRING_BULDER);

                // Style background fill
                lineSet.setFill(false);
                lineSet.setSmooth(true);

                statLines.add(lineSet);
            }

            Paint mLineGridPaint = new Paint();
            mLineGridPaint.setColor(context.getResources().getColor(R.color.text_color));
            mLineGridPaint.setAntiAlias(true);
            mLineGridPaint.setStrokeWidth(Tools.fromDpToPx(.10f));

            chartView.setXAxis(true).setYAxis(true).setGrid(LineChartView.GridType.FULL, mLineGridPaint);
            chartView.setAxisBorderValues(MIN_VALUE, MAX_VALUE, STEP);

            chartView.setFontSize(30);

            chartView.addData(statLines);

            return chartView;
        }

        return null;
    }


    public SparseArray getPlayersColorSparse() {
        return playersColorSparse;
    }
}
