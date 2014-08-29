package com.g_art.munchkinlevelcounter.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by G_Art on 2/8/2014.
 */
public class LineFragment extends Fragment {

    final String TAG = "GameActivity_Munchkin_Test";
    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";
    private Random rnd;
    private LineChartView chartView;
    private HashMap playersColor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);

            Log.d(TAG, "FragmentPlayersList get beans: " + playersList.toString());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_linegraph, container, false);


        chartView = (LineChartView) v.findViewById(R.id.LineChart);

        LineSet lineSet;
        ArrayList<ChartSet> lvlLines = new ArrayList<ChartSet>();
        playersColor = new HashMap();


        for (Player player : playersList) {
            lineSet = new LineSet();


            for (int i = 0; i < player.getPowerStats().size(); i++) {
                lineSet.addPoint(new Point(String.valueOf(i), Integer.parseInt(player.getPowerStats().get(i))));

            }

            // Style dots
            lineSet.setDots(true);
            rnd = new Random();
            lineSet.setDotsColor(getResources().getColor(R.color.blue));
            lineSet.setDotsStrokeColor(getResources().getColor(R.color.green_light));

            // Style line
//        lineSet.setLineThickness(0.9f);
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            lineSet.setLineColor(color);
            playersColor.put(color, player);

            // Style background fill
            lineSet.setFill(false);

            lineSet.setLineSmooth(true);
            chartView.setGrid(true);

            lvlLines.add(lineSet);
        }

        chartView.addData(lvlLines);


        chartView.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int setIndex, int entryIndex) {
                Toast.makeText(getActivity(), "Test Toast click on char/dot setIndex: " + setIndex + " entryIndex: " + entryIndex, Toast.LENGTH_SHORT).show();
            }
        });


        chartView.setAnimation(new Animation());

        chartView.show();


        return v;
    }
}
