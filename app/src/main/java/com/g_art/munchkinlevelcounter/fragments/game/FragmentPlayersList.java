package com.g_art.munchkinlevelcounter.fragments.game;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.listadapter.InGamePlayersAdapter;
import com.g_art.munchkinlevelcounter.listadapter.helper.ItemClickSupport;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.ArrayList;

public class FragmentPlayersList extends Fragment {

	private static final String PLAYER_KEY = "playersList";
	private ArrayList<Player> playersList;

	private RecyclerView mRecyclerView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private OnPlayerSelectedListener mCallback;
	private InGamePlayersAdapter inGameAdapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public FragmentPlayersList() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

		if (getArguments() != null) {
			playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
		}
		inGameAdapter = new InGamePlayersAdapter(playersList);
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
					+ " must implement OnPlayerSelectedListener");
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
		return inflater.inflate(R.layout.fragment_players_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_in_game_players);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(inGameAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		final ItemClickSupport itemClick = ItemClickSupport.addTo(mRecyclerView);
		itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
			@Override
			public void onItemClicked(RecyclerView recyclerView, int position, View v) {
				mCallback.onPlayerSelected(position);
				selectPlayer(position);
			}
		});
	}

	public void selectPlayer(int position) {
		scrollToPlayer(position);
		inGameAdapter.clearSelection();
		inGameAdapter.toggleSelection(position);
	}

	public void updatePlayer(int position) {
		inGameAdapter.notifyItemChanged(position);
	}

	private void scrollToPlayer(int position) {
		mRecyclerView.scrollToPosition(position);
	}

	// interface for communication between fragments
	public interface OnPlayerSelectedListener {
		void onPlayerSelected(int position);
	}
}