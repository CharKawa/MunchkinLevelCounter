package com.g_art.munchkinlevelcounter.fragments.stats.datahandler;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.db.chart.OnEntryClickListener;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by G_Art on 3/9/2014.
 */
public class StatsHandler {
    final String TAG = "GameActivity_Munchkin_Test";
    private ArrayList<Player> playersList;
    private LineChartView chartView;
    private Random rnd;
    private HashMap playersColor;
    public final static int LVL_STATS = 0;
    public final static int GEAR_STATS = 1;
    public final static int POWER_STATS = 2;


    public StatsHandler(ArrayList<Player> playersList, LineChartView chartView) {
        this.playersList = playersList;
        this.chartView = chartView;
        rnd = new Random();

    }

    public LineChartView getStats(final Context context, int stats) {

        playersColor = new HashMap();

        LineSet lineSet;
        ArrayList<ChartSet> statLines = new ArrayList<ChartSet>();

        if (playersList != null) {
            for (Player player : playersList) {

                lineSet = new LineSet();

                switch (stats) {
                    case LVL_STATS:
                        Log.d(TAG, "getLvlStats: " + player.getLvlStats().size());
                        for (int i = 0; i < player.getLvlStats().size(); i++) {
                            lineSet.addPoint(new Point(String.valueOf(i), Integer.parseInt(player.getLvlStats().get(i))));
                        }
                        break;
                    case GEAR_STATS:
                        Log.d(TAG, "getGearStats: " + player.getGearStats().size());
                        for (int i = 0; i < player.getGearStats().size(); i++) {
                            Log.d(TAG, "Integer.parseInt: " + Integer.parseInt(player.getGearStats().get(i)));
                            lineSet.addPoint(new Point(String.valueOf(i), Integer.parseInt(player.getGearStats().get(i))));
                        }
                        break;
                    case POWER_STATS:
                        Log.d(TAG, "getPowerStats: " + player.getPowerStats());
                        for (int i = 0; i < player.getPowerStats().size(); i++) {
                            lineSet.addPoint(new Point(String.valueOf(i), Integer.parseInt(player.getPowerStats().get(i))));
                        }
                        break;
                    default:
                        for (int i = 0; i < player.getLvlStats().size(); i++) {
                            lineSet.addPoint(new Point(String.valueOf(i), Integer.parseInt(player.getLvlStats().get(i))));
                        }
                        break;
                }

                // Style dots
                lineSet.setDots(true);
                lineSet.setDotsColor(context.getResources().getColor(R.color.blue));
                lineSet.setDotsStrokeColor(context.getResources().getColor(R.color.green_light));

                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                lineSet.setLineColor(color);
                playersColor.put(color, player.getName());

                // Style background fill
                lineSet.setFill(false);

                lineSet.setLineSmooth(true);
                chartView.setGrid(true);

                Log.d(TAG, "lineSet trying to add");

                statLines.add(lineSet);
                Log.d(TAG, "lineSet added");


            }

            Log.d(TAG, "statLines trying to add");

            chartView.addData(statLines);
            Log.d(TAG, "statLines added");


            chartView.setOnEntryClickListener(new OnEntryClickListener() {
                @Override
                public void onClick(int setIndex, int entryIndex) {
                    Toast.makeText(context, "Test Toast click on char/dot setIndex: " + setIndex + " entryIndex: " + entryIndex, Toast.LENGTH_SHORT).show();
                }
            });


            chartView.setAnimation(new Animation());
            return chartView;
        }

        return null;
    }

    public HashMap getPlayersColor() {
        return playersColor;
    }
}
