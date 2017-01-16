package com.g_art.munchkinlevelcounter.listadapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agulia on 12/1/16.
 */

public class InGamePlayersAdapter extends RecyclerView.Adapter<InGamePlayersAdapter.ViewHolder> {

	private List<Player> mPlayers;
	private SparseBooleanArray selectedItems;

	public InGamePlayersAdapter(List<Player> mPlayers) {
		this.mPlayers = mPlayers;
		selectedItems = new SparseBooleanArray();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_game_players_row, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Player player = mPlayers.get(position);
		holder.pName.setText(player.getName());
		holder.pLvl.setText(String.valueOf(player.getLevel()));
		holder.pTotal.setText(String.valueOf(player.getPower()));

		if (isSelected(position)) {
			highlightSelection(holder.itemView);
		} else {
			unhighlightSelection(holder.itemView);
		}

	}

	@Override
	public int getItemCount() {
		return mPlayers.size();
	}

	/**
	 * Indicates if the item at position position is selected
	 *
	 * @param position Position of the item to check
	 * @return true if the item is selected, false otherwise
	 */
	public boolean isSelected(int position) {
		return getSelectedItems().contains(position);
	}

	/**
	 * Toggle the selection status of the item at a given position
	 *
	 * @param position Position of the item to toggle the selection status for
	 */
	public void toggleSelection(int position) {
		if (selectedItems.get(position, false)) {
			selectedItems.delete(position);
		} else {
			selectedItems.put(position, true);
		}
		notifyItemChanged(position);
	}

	/**
	 * Clear the selection status for all items
	 */
	public void clearSelection() {
		final List<Integer> selection = getSelectedItems();
		selectedItems.clear();
		for (Integer i : selection) {
			notifyItemChanged(i);
		}
	}

	/**
	 * Count the selected items
	 *
	 * @return Selected items count
	 */
	public int getSelectedItemCount() {
		return selectedItems.size();
	}

	/**
	 * Indicates the list of selected items
	 *
	 * @return List of selected items ids
	 */
	public List<Integer> getSelectedItems() {
		final List<Integer> items = new ArrayList<>(selectedItems.size());
		for (int i = 0; i < selectedItems.size(); ++i) {
			items.add(selectedItems.keyAt(i));
		}
		return items;
	}

	private void highlightSelection(View rowView) {
		rowView.setBackgroundColor(rowView.getResources().getColor(R.color.list_selected));

		TextView name = (TextView) rowView.findViewById(R.id.in_game_player_name);
		name.setTextColor(rowView.getResources().getColor(R.color.actionBarText_color));
		TextView lvl = (TextView) rowView.findViewById(R.id.in_game_player_lvl);
		lvl.setTextColor(rowView.getResources().getColor(R.color.actionBarText_color));
		TextView lvlValue = (TextView) rowView.findViewById(R.id.in_game_player_lvl_value);
		lvlValue.setTextColor(rowView.getResources().getColor(R.color.actionBarText_color));
		TextView total = (TextView) rowView.findViewById(R.id.in_game_player_total);
		total.setTextColor(rowView.getResources().getColor(R.color.actionBarText_color));
		TextView totalValue = (TextView) rowView.findViewById(R.id.in_game_player_total_value);
		totalValue.setTextColor(rowView.getResources().getColor(R.color.actionBarText_color));
	}

	private void unhighlightSelection(View rowView) {
		rowView.setBackgroundColor(Color.TRANSPARENT);

		TextView name = (TextView) rowView.findViewById(R.id.in_game_player_name);
		name.setTextColor(rowView.getResources().getColor(R.color.text_color));
		TextView lvl = (TextView) rowView.findViewById(R.id.in_game_player_lvl);
		lvl.setTextColor(rowView.getResources().getColor(R.color.text_color));
		TextView lvlValue = (TextView) rowView.findViewById(R.id.in_game_player_lvl_value);
		lvlValue.setTextColor(rowView.getResources().getColor(R.color.text_color));
		TextView total = (TextView) rowView.findViewById(R.id.in_game_player_total);
		total.setTextColor(rowView.getResources().getColor(R.color.text_color));
		TextView totalValue = (TextView) rowView.findViewById(R.id.in_game_player_total_value);
		totalValue.setTextColor(rowView.getResources().getColor(R.color.text_color));
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView pName;
		TextView pLvl;
		TextView pTotal;

		public ViewHolder(View itemView) {
			super(itemView);
			pName = (TextView) itemView.findViewById(R.id.in_game_player_name);
			pLvl = (TextView) itemView.findViewById(R.id.in_game_player_lvl_value);
			pTotal = (TextView) itemView.findViewById(R.id.in_game_player_total_value);
		}

	}

}
