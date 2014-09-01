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
import android.widget.Toast;

import com.db.chart.OnEntryClickListener;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.google.gson.Gson;
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
    final String PLAYER_KEY = "playersList";
    final String PREFS_PLAYERS_KEY = "players";
    final String PREFS_NO_DATA = "Sorry, No Data!";
    private Random rnd;
    private LineChartView chartView;
    private HashMap playersColor;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
            if (playersList != null) {
                Log.d(TAG, "FragmentPlayersList get beans: " + playersList.toString());

                clearSharedPrefs();
                savePlayersToSharedPrefs();
            } else {
                getPlayersFromSharedPrefs();
            }
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

    private boolean clearSharedPrefs() {
        boolean result;

        prefsEditor.clear();
        try {
            prefsEditor.apply();
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;

    }

    private boolean savePlayersToSharedPrefs() {
        Gson gson = new Gson();
        boolean result;

        String json = gson.toJson(playersList);
        Log.d(TAG, json);
        prefsEditor.putString(PREFS_PLAYERS_KEY, json);
        result = prefsEditor.commit();


        return result;
    }

    private void getPlayersFromSharedPrefs() {
        playersList = new ArrayList<Player>();
        Gson gson = new Gson();
        String json = mPrefs.getString(PREFS_PLAYERS_KEY, PREFS_NO_DATA);
        Type type = new TypeToken<ArrayList<Player>>() {
        }.getType();
        playersList = gson.fromJson(json, type);
        Log.d(TAG, playersList.toString());

    }
}
