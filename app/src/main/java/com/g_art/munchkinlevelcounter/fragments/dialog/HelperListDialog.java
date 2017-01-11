package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Munchkin_Dialog);

		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View v = inflater.inflate(R.layout.helper_dialog, null);

		//Binding views
		mRecyclerView = (RecyclerView)v.findViewById(R.id.helper_list_rec_view);

		final ArrayList<Player> helperList = getArguments().getParcelableArrayList(BattleActivity.HELPER_LIST);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

		mRecyclerView.setLayoutManager(layoutManager);

		HelperListAdapter mAdapter = new HelperListAdapter(helperList);
		mRecyclerView.setAdapter(mAdapter);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
				LinearLayout.VERTICAL);
		mRecyclerView.addItemDecoration(dividerItemDecoration);

		builder.setTitle(R.string.title_dialog_choose_helper)
				.setView(v);

		return builder.create();
	}

}
