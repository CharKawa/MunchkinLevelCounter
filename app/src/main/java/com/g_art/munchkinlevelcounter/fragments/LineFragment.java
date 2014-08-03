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

import java.util.ArrayList;


/**
 * Created by G_Art on 2/8/2014.
 */
public class LineFragment extends Fragment {

    final String TAG = "Munchkin";
    LinePoint p = null;
    ArrayList<LinePoint> secondPlayerPoints;
    ArrayList<LinePoint> firstPlayerPoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_linegraph, container, false);
        final Resources resources = getResources();
        Line firstPlayerLine = new Line();
        Line secondPlayerLine = new Line();
        firstPlayerLine.setUsingDips(false);

        firstPlayerPoints = new ArrayList<LinePoint>();

        for (int i = 0, j = 0; i <= 10; i++) {
            p = new LinePoint(i, j);
            p.setColor(resources.getColor(R.color.red));
            firstPlayerPoints.add(p);

            Log.d(TAG, "Number i & j at firstPlayerPoints: " + i + " " + j);
            if (j % 2 == 0) {
                j += 3;
            } else {
                j -= 1;
            }

        }
        ;


        secondPlayerPoints = new ArrayList<LinePoint>();

        for (int i = 0, j = 0; i <= 10; i++) {
            p = new LinePoint(i, j);
            p.setColor(resources.getColor(R.color.blue));
            secondPlayerPoints.add(p);
            Log.d(TAG, "Number i & j at secondPlayerPoints: " + i + " " + j);
            j += 1;
        }

        firstPlayerLine.setPoints(firstPlayerPoints);
        firstPlayerLine.setColor(resources.getColor(R.color.orange));
        firstPlayerLine.setUsingDips(false);
        secondPlayerLine.setPoints(secondPlayerPoints);
        secondPlayerLine.setColor(resources.getColor(R.color.green));
        secondPlayerLine.setUsingDips(false);

        LineGraph li = (LineGraph) v.findViewById(R.id.linegraph);
        li.addLine(firstPlayerLine);
        li.addLine(secondPlayerLine);
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
