package com.g_art.munchkinlevelcounter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;

import java.util.ArrayList;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LineFragment extends Fragment {

    final String TAG = "Munchkin";
    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";


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

        LineChartView chartView = (LineChartView) v.findViewById(R.id.LineChart);

        LineSet lineSet = new LineSet();
        lineSet.addPoint(new Point("1", 2));
        lineSet.addPoint(new Point("2", 5));
        lineSet.addPoint(new Point("3", 5));
        lineSet.addPoint(new Point("4", 6));
        lineSet.addPoint(new Point("5", 3));
        lineSet.addPoint(new Point("6", 1));
        lineSet.addPoint(new Point("7", 7));

        // Style dots
        lineSet.setDots(true);
        lineSet.setDotsColor(getResources().getColor(R.color.blue));
        lineSet.setDotsStrokeColor(getResources().getColor(R.color.green_light));

        // Style line
        lineSet.setLineThickness(0.5f);
        lineSet.setLineColor(getResources().getColor(R.color.green));

        // Style background fill
        lineSet.setFill(false);
//        lineSet.setFillColor(getResources().getColor(R.color.transparent_orange));

        lineSet.setLineDashed(true);
        lineSet.setLineSmooth(true);
        chartView.addData(lineSet);
        chartView.show();

        return v;
    }
}
