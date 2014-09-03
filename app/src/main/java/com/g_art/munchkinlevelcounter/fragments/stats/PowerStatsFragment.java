package com.g_art.munchkinlevelcounter.fragments.stats;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.db.chart.view.LineChartView;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;
import com.g_art.munchkinlevelcounter.listadapter.InStatsPlayerListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by G_Art on 2/8/2014.
 */
public class PowerStatsFragment extends Fragment {

    final String TAG = "GameActivity_Munchkin_Test";
    private ArrayList<Player> playersList;
    final static String PLAYER_KEY = "playersList";
    final static String PREFS_PLAYERS_KEY = "players";
    public final static String PREFS_NO_DATA = "Sorry, No Data!";
    private Random rnd;
    private LineChartView chartView;
    private HashMap playersColor;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;
    private boolean isDataPresent;
    private InStatsPlayerListAdapter inStatsAdapter;
    private ListView inStatsPlayersList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        isDataPresent = false;

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
            if (playersList != null) {
                Log.d(TAG, "PowerStatsFr gets playerList: " + playersList.toString());
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
            chartView = (LineChartView) v.findViewById(R.id.LineChart);

            StatsHandler statsHandler = new StatsHandler(playersList, chartView);
            chartView = statsHandler.getStats(getActivity(), StatsHandler.POWER_STATS);
            playersColor = statsHandler.getPlayersColor();
            chartView.show();

        } else {
            Toast.makeText(getActivity(), PREFS_NO_DATA, Toast.LENGTH_SHORT).show();
        }

        inStatsAdapter = new InStatsPlayerListAdapter(getActivity(), playersColor);
        inStatsPlayersList = (ListView) v.findViewById(R.id.isStatsPlayersList);
        inStatsPlayersList.setAdapter(inStatsAdapter);

        return v;
    }
}
