package com.g_art.munchkinlevelcounter.fragments.stats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.db.chart.view.LineChartView;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.SparseStringsAdapter;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;

import java.util.ArrayList;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LvlStatsFragment extends Fragment {

    final static String PLAYER_KEY = "playersList";
    private ArrayList<Player> playersList;
    private SparseArray playersColors;
    private boolean isDataPresent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDataPresent = false;
        playersColors = new SparseArray();

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
            if (playersList != null && !playersList.isEmpty()) {
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

        if (isDataPresent) {
            LineChartView chartView = (LineChartView) v.findViewById(R.id.LineChart);

            StatsHandler statsHandler = new StatsHandler(playersList, chartView);
            chartView = statsHandler.getStats(getActivity(), StatsHandler.LVL_STATS);
            if (chartView != null) {
                playersColors = statsHandler.getPlayersColorSparse();
                chartView.show();
            }

        }

        SparseStringsAdapter sparseStringsAdapter = new SparseStringsAdapter(getActivity(), playersColors);
        ListView inStatsPlayersList = (ListView) v.findViewById(R.id.isStatsPlayersList);
        inStatsPlayersList.setAdapter(sparseStringsAdapter);
        return v;
    }
}
