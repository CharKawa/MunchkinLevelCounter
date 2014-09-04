package com.g_art.munchkinlevelcounter.fragments.stats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.db.chart.view.LineChartView;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.Stats;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;
import com.g_art.munchkinlevelcounter.listadapter.InStatsPlayerListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LvlStatsFragment extends Fragment {

    final String TAG = "GameActivity_Munchkin_Test";
    private ArrayList<Player> playersList;
    final static String PLAYER_KEY = "playersList";
    private Random rnd;
    private LineChartView chartView;
    private HashMap playersColor;
    private boolean isDataPresent;
    private InStatsPlayerListAdapter inStatsAdapter;
    private ListView inStatsPlayersList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDataPresent = false;
        rnd = new Random();

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
            if (playersList.size() > 0) {
                Log.d(TAG, "LvlStatsFr gets playersList: " + playersList.toString());
                isDataPresent = true;
            } else {
                isDataPresent = false;

            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_linegraph, container, false);
        Log.d(TAG, "isDataPresent: " + isDataPresent);

        if (isDataPresent) {
            chartView = (LineChartView) v.findViewById(R.id.LineChart);

            StatsHandler statsHandler = new StatsHandler(playersList, chartView);
            chartView = statsHandler.getStats(getActivity(), StatsHandler.LVL_STATS);
            if (chartView != null) {
                playersColor = statsHandler.getPlayersColor();
                chartView.show();


            }

        } else {
            Toast.makeText(getActivity(), Stats.PREFS_NO_DATA, Toast.LENGTH_SHORT).show();
        }


        inStatsAdapter = new InStatsPlayerListAdapter(getActivity(), playersColor);
        inStatsPlayersList = (ListView) v.findViewById(R.id.isStatsPlayersList);
        inStatsPlayersList.setAdapter(inStatsAdapter);
        return v;
    }
}
