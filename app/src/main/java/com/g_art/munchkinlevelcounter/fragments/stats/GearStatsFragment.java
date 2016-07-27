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
import com.g_art.munchkinlevelcounter.activity.Stats;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.SparseStringsAdapter;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.ArrayList;


/**
 * Created by G_Art on 2/8/2014.
 */
public class GearStatsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_linegraph, container, false);

        boolean isDataPresent = false;
        ArrayList<Player> playersList = null;
        SparseArray<String> playersColors = new SparseArray<>();

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(Stats.PLAYER_KEY);
            isDataPresent = playersList != null && !playersList.isEmpty();
        }

        if (isDataPresent) {
            LineChartView chartView = (LineChartView) v.findViewById(R.id.LineChart);

            playersColors = StatsHandler.getStats(playersList, chartView, getActivity(), StatsHandler.GEAR_STATS);
            if (playersColors != null) {
                chartView.show();
            }
        }

        SparseStringsAdapter sparseStringsAdapter = new SparseStringsAdapter(getActivity(), playersColors);
        ListView inStatsPlayersList = (ListView) v.findViewById(R.id.isStatsPlayersList);
        inStatsPlayersList.setAdapter(sparseStringsAdapter);

        return v;
    }
}
