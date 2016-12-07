package com.g_art.munchkinlevelcounter.listadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.ArrayList;

/**
 * Created by agulia on 12/1/16.
 */

public class HelperListAdapter extends RecyclerView.Adapter<HelperListAdapter.VieHolder> {

	private ArrayList<Player> mPlayers;

	public HelperListAdapter(ArrayList<Player> mPlayers) {
		this.mPlayers = mPlayers;
	}

	@Override
	public VieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_row, parent, false);
		return new VieHolder(v);
	}

	@Override
	public void onBindViewHolder(VieHolder holder, int position) {
		final Player helper = mPlayers.get(position);
		holder.helperName.setText(helper.getName());
		holder.helperPower.setText(String.valueOf(helper.getLevel()+helper.getGear()));

	}

	@Override
	public int getItemCount() {
		return mPlayers.size();
	}

	public static class VieHolder extends RecyclerView.ViewHolder {

		TextView helperName;
		TextView helperPower;

		public VieHolder(View itemView) {
			super(itemView);
			helperName = (TextView) itemView.findViewById(R.id.helper_name);
			helperPower= (TextView) itemView.findViewById(R.id.helper_power);
		}
	}
}
