package com.g_art.munchkinlevelcounter.listadapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.model.Sex;

import java.util.ArrayList;

/**
 * Created by agulia on 11/27/16.
 */

public class NewPlayersRecyclerAdapter extends RecyclerView.Adapter<NewPlayersRecyclerAdapter.ViewHolder> {

	private ArrayList<Player> mPlayers;

	public NewPlayersRecyclerAdapter(ArrayList<Player> mPlayers) {
		this.mPlayers = mPlayers;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.new_players_row, parent, false);

		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		if (mPlayers.isEmpty()) {
			holder.text.setText(R.string.no_players);

			holder.imBtnPlayerSex.setVisibility(View.GONE);
		} else {

			final Player player = mPlayers.get(holder.getAdapterPosition());

			holder.text.setText(player.getName());
//			holder.mTextWatcher.updatePosition(holder.getAdapterPosition());

			holder.text.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					mPlayers.get(holder.getAdapterPosition()).setName(s.toString());
				}
			});
			holder.imBtnPlayerSex.setVisibility(View.VISIBLE);
			holder.imBtnPlayerSex.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					playerChangeSex(holder.getAdapterPosition());
				}
			});

			if (player.getSex() == Sex.MAN) {
				holder.imBtnPlayerSex.setImageResource(R.drawable.man);
			} else {
				holder.imBtnPlayerSex.setImageResource(R.drawable.woman);
			}
		}
	}

	private void playerChangeSex(int playerPosition) {
		final Player player = mPlayers.get(playerPosition);
		if (Sex.MAN == player.getSex()) {
			player.setSex(Sex.WOMAN);
		} else {
			player.setSex(Sex.MAN);
		}
		notifyItemChanged(playerPosition);
	}

	@Override
	public int getItemCount() {
		return mPlayers.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		private EditText text;
		private ImageButton imBtnPlayerSex;

		public ViewHolder(View itemView) {
			super(itemView);

			text = (EditText) itemView.findViewById(R.id.newPlayerName);
			imBtnPlayerSex = (ImageButton) itemView.findViewById(R.id.imvPlayerSex);
		}
	}
}
