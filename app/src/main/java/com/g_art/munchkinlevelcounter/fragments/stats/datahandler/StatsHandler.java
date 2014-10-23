package com.g_art.munchkinlevelcounter.fragments.stats.datahandler;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;

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
    final String TAG = "GameActivity_Munchkin_Test";
    private final static int CLEAR_STRING_BULDER = 0;
    private ArrayList<Player> playersList;
    private LineChartView chartView;
    private static final Random rnd = new Random();
    private SparseArray<String> playersColorSparse;
    private StringBuilder s;
    public final static int LVL_STATS = 0;
    public final static int GEAR_STATS = 1;
    public final static int POWER_STATS = 2;


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
                        for (int i = 0; i < player.getLvlStats().size(); i++) {
                            lineSet.addPoint(new Point(String.valueOf(" "), Integer.parseInt(player.getLvlStats().get(i))));
                        }
                        s.append(" ").append(lvl).append(": ").append(player.getLevel());
                        break;
                    case GEAR_STATS:
                        boolean gearChanged = false;
                        for (int i = 0; i < player.getGearStats().size(); i++) {
                            int gearStat = Integer.parseInt(player.getGearStats().get(i));
                            lineSet.addPoint(new Point(String.valueOf(" "), gearStat));

                            if (gearStat != 0) {
                                gearChanged = true;
                            }
                        }
                        if (!gearChanged) {
                            lineSet.addPoint(new Point(String.valueOf(" "), GEAR_STATS));
                        }
                        s.append(" ").append(gear).append(": ").append(player.getGear());
                        break;
                    case POWER_STATS:
                        for (int i = 0; i < player.getPowerStats().size(); i++) {
                            lineSet.addPoint(new Point(String.valueOf(" "), Integer.parseInt(player.getPowerStats().get(i))));
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

                lineSet.setLineSmooth(true);
                statLines.add(lineSet);
            }

            chartView.setLabels(false);
            chartView.setGrid(true);
            chartView.addData(statLines);

//            chartView.setAnimation(new Animation(1000));
            return chartView;
        }

        return null;
    }


    public SparseArray getPlayersColorSparse() {
        return playersColorSparse;
    }
}
