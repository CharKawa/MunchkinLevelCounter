package com.g_art.munchkinlevelcounter.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;

import java.util.ArrayList;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LineFragment extends Fragment {

    final String TAG = "Munchkin";
    LinePoint p = null;
    private ArrayList<Player> playersList;
    final String PLAYER_KEY = "playersList";
    ArrayList<LinePoint> secondPlayerPoints;
    ArrayList<LinePoint> firstPlayerPoints;


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
        final Resources resources = getResources();


        firstPlayerPoints = new ArrayList<LinePoint>();
        Player player = playersList.get(0);

        ArrayList<String> list = player.getLvlStats();


        for (int i = 0; i < list.size(); i++) {
            p = new LinePoint(i, Integer.parseInt(list.get(i)));
            p.setColor(resources.getColor(R.color.red));
            firstPlayerPoints.add(p);

            Log.d(TAG, "Building points: " + list.get(i));

        }

        Line firstPlayerLine = new Line();

        firstPlayerLine.setPoints(firstPlayerPoints);
        firstPlayerLine.setColor(resources.getColor(R.color.orange));
        firstPlayerLine.setUsingDips(false);

        LineGraph li = (LineGraph) v.findViewById(R.id.linegraph);
        li.addLine(firstPlayerLine);
        li.setRangeY(0, 15);
        li.setRangeX(0, 15);


        li.setOnPointClickedListener(new LineGraph.OnPointClickedListener() {

            @Override
            public void onClick(int lineIndex, int pointIndex) {
                Toast.makeText(getActivity(),
                        "Line " + lineIndex + " / Point " + pointIndex + " clicked",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        return v;
    }
}
