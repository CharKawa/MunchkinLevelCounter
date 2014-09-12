package com.g_art.munchkinlevelcounter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.listadapter.InGameListAdapter;
import com.g_art.munchkinlevelcounter.settings.SettingsHandler;

import java.util.ArrayList;

public class FragmentPlayersList extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<Player> playersList;
    private static final String PLAYER_KEY = "playersList";
    final String TAG = "GameActivity_Munchkin_Test";
    private Player player;
    private View view;
    private boolean collectStats;


    /**
     * The fragment's ListView/GridView.
     */
    private ListView inGamePlayersList;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private InGameListAdapter inGameAdapter;
    private OnPlayerSelectedListener mCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentPlayersList() {
    }

    // interface for communication between fragments
    public interface OnPlayerSelectedListener {
        public void onPlayerSelected(Player player);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
            collectStats = getArguments().getBoolean(SettingsHandler.STATS_SETTINGS, true);
        }
        inGameAdapter = new InGameListAdapter(getActivity(), playersList);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPlayerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PLAYER_KEY, playersList);
    }


    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if(view==null){
        view = inflater.inflate(R.layout.fragment_players_list, container, false);
//        }

        // Set the adapter
        inGamePlayersList = (ListView) view.findViewById(R.id.inGamePlayersList);
        inGamePlayersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        inGamePlayersList.setAdapter(inGameAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        inGamePlayersList.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        player = playersList.get(position);
        inGamePlayersList.setItemChecked(position, true);
        mCallback.onPlayerSelected(player);

    }

    public void listUpdate() {
        inGameAdapter.notifyDataSetChanged();
    }


}