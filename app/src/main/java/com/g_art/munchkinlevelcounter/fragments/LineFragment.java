package com.g_art.munchkinlevelcounter.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.db.chart.OnEntryClickListener;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.listadapter.InStatsPlayerListAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LineFragment extends Fragment {

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
        rnd = new Random();

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
            if (playersList != null) {
                Log.d(TAG, "FragmentPlayersList get beans: " + playersList.toString());
                isDataPresent = true;
                clearSharedPrefs();
                Log.d(TAG, "Deleting prev stats from shared prefs");
                savePlayersToSharedPrefs();
                Log.d(TAG, "Saving stats to shared prefs");
            } else {
                isDataPresent = getPlayersFromSharedPrefs();
                Log.d(TAG, "Getting stats from shared prefs");

            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_linegraph, container, false);
        playersColor = new HashMap();

        if (isDataPresent) {
            chartView = (LineChartView) v.findViewById(R.id.LineChart);

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

                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                lineSet.setLineColor(color);
                playersColor.put(color, player.getName());

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

        } else {
            Toast.makeText(getActivity(), PREFS_NO_DATA, Toast.LENGTH_SHORT).show();
        }

        inStatsAdapter = new InStatsPlayerListAdapter(getActivity(), playersColor);
        inStatsPlayersList = (ListView) v.findViewById(R.id.isStatsPlayersList);
        inStatsPlayersList.setAdapter(inStatsAdapter);

        return v;
    }

    /**
     * Removing previous results of statistic in shared prefs before storing new one
     *
     * @return boolean. True for success, false for failure.
     */
    private boolean clearSharedPrefs() {
        boolean result;
        prefsEditor.remove(PREFS_PLAYERS_KEY);
        try {
            prefsEditor.apply();
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;

    }

    /**
     * Saving players stats to shared prefs in json format, using Gson from Google(deprecated)
     * @return boolean. True for success, false for failure.
     */
    private boolean savePlayersToSharedPrefs() {
        Gson gson = new Gson();
        boolean result;

        String json = gson.toJson(playersList);
        Log.d(TAG, json);
        prefsEditor.putString(PREFS_PLAYERS_KEY, json);
        result = prefsEditor.commit();


        return result;
    }

    /**
     * Getting players stats from shared prefs.
     *
     * @return boolean. True for success, false for failure.
     */
    private boolean getPlayersFromSharedPrefs() {
        boolean result = false;
        playersList = new ArrayList<Player>();
        Gson gson = new Gson();
        String json = mPrefs.getString(PREFS_PLAYERS_KEY, PREFS_NO_DATA);
        if (!json.equals(PREFS_NO_DATA)) {
            Type type = new TypeToken<ArrayList<Player>>() {
            }.getType();
            try {
                playersList = gson.fromJson(json, type);
                Log.d(TAG, playersList.toString());
                result = true;
            } catch (JsonSyntaxException jsonSyntaxEx) {
                Log.d(TAG, jsonSyntaxEx.toString());
                result = false;
            } catch (IllegalStateException illegalStateEx) {
                Log.d(TAG, illegalStateEx.toString());
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }
}
