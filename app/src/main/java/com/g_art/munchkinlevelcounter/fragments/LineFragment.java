package com.g_art.munchkinlevelcounter.fragments;

import android.app.Fragment;
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
//            if(playersList.size()==0){
//                playersList = new ArrayList<Player>();
//                playersList.add(new Player("Test"));
//            }
            Log.d(TAG, "FragmentPlayersList get beans: " + playersList.toString());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_linegraph, container, false);

        LineChartView chartView = (LineChartView) v.findViewById(R.id.LineChart);

        LineSet lineSet;
        ArrayList<ChartSet> lvlLines = new ArrayList<ChartSet>();


        for (Player player : playersList) {
            lineSet = new LineSet();

            for (int i = 0; i < player.getLvlStats().size(); i++) {
                lineSet.addPoint(new Point(String.valueOf(i), Integer.parseInt(player.getLvlStats().get(i))));

            }

            // Style dots
            lineSet.setDots(true);
            lineSet.setDotsColor(getResources().getColor(R.color.blue));
            lineSet.setDotsStrokeColor(getResources().getColor(R.color.green_light));

            // Style line
//        lineSet.setLineThickness(0.9f);
            lineSet.setLineColor(getResources().getColor(R.color.green));

            // Style background fill
            lineSet.setFill(false);

            lineSet.setLineSmooth(true);
            chartView.setGrid(true);

            lvlLines.add(lineSet);
        }

        chartView.addData(lvlLines);

//        ArrayList<String> firstPlayer = playersList.get(0).getLvlStats();
//        if(!firstPlayer.isEmpty()){
//            int length = firstPlayer.size();
//
//        }


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
