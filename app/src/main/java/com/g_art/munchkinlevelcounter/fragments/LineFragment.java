package com.g_art.munchkinlevelcounter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return v;
    }
}
