package com.g_art.munchkinlevelcounter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.listadapter.InGameListAdapter;

import java.util.ArrayList;

public class FragmentPlayersList extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<Player> playersList;
    private static final String PLAYER_KEY = "playersList";
    //    final String TAG = "Munchkin_FragmentPlayersList";
    final String TAG = "GameActivity_Munchkin_Test";
    private Player player;
    private View view;

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

        Log.d(TAG, "onCreate method");
        // Retain this fragment across configuration changes.
        setRetainInstance(true);

//        if(savedInstanceState == null || !savedInstanceState.containsKey(PLAYER_KEY)){
//            playersList = savedInstanceState.getParcelableArrayList(PLAYER_KEY);
//
//        }else{

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
            Log.d(TAG, "FragmentPlayersList get bean: " + playersList.toString());
        }
//        }
        inGameAdapter = new InGameListAdapter(getActivity(), playersList);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach method");

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated method");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PLAYER_KEY, playersList);
        Log.d(TAG, "onSaveInstanceState method");

    }

//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//    }


    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
        Log.d(TAG, "setRetainInstance method");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume method");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart method");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause method");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop method");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy method");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView method");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach method");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if(view==null){
        view = inflater.inflate(R.layout.fragment_players_list, container, false);
//        }

        // Set the adapter
        inGamePlayersList = (ListView) view.findViewById(R.id.inGamePlayersList);
        inGamePlayersList.setAdapter(inGameAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        inGamePlayersList.setOnItemClickListener(this);

        Log.d(TAG, "onCreateView method");

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        player = playersList.get(position);
        Toast.makeText(getActivity(), "Test position " + position + "player: " + player.toString(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick method: List was clicked : " + player.toString());
        mCallback.onPlayerSelected(player);

    }

    public void listUpdate() {
        inGameAdapter.notifyDataSetChanged();
        Log.d(TAG, "listUpdate method: Adapter notified about update");
    }


}