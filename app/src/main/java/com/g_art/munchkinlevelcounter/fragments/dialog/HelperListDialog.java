package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.BattleActivity;
import com.g_art.munchkinlevelcounter.listadapter.HelperListAdapter;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by agulia on 11/29/16.
 */

public class HelperListDialog extends DialogFragment {
	private RecyclerView mRecyclerView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.helper_dialog, container, false);

		//Binding views
		mRecyclerView = (RecyclerView)v.findViewById(R.id.helper_list_rec_view);

		final ArrayList<Player> helperList = getArguments().getParcelableArrayList(BattleActivity.HELPER_LIST);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		HelperListAdapter mAdapter = new HelperListAdapter(helperList);
		mRecyclerView.setAdapter(mAdapter);

		return v;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
