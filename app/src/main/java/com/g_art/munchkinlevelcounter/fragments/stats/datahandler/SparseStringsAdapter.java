package com.g_art.munchkinlevelcounter.fragments.stats.datahandler;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;

/**
 * Created by G_Art on 7/9/2014.
 */
public class SparseStringsAdapter extends SparseArrayAdapter<String> {

	private final LayoutInflater mInflater;

	public SparseStringsAdapter(Context context, SparseArray<String> data) {
		mInflater = LayoutInflater.from(context);
		setData(data);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;

		if (view == null) {
			view = mInflater.inflate(R.layout.in_stat_player_list, null);

			holder = new ViewHolder();
			holder.txtPlayerName = (TextView) view.findViewById(R.id.txtPlayerNameInStats);
			holder.viewPlayerColor = view.findViewById(R.id.playerColor);

			/************  Set holder with LayoutInflater ************/
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (getCount() == 0) {
			holder.txtPlayerName.setText(view.getContext().getString(R.string.no_data));
			holder.viewPlayerColor.setVisibility(View.INVISIBLE);
		} else {
			holder.viewPlayerColor.setBackgroundColor((int) getItemId(position));
			holder.txtPlayerName.setText(getItem(position));
		}

		return view;
	}

	public static class ViewHolder {
		public View viewPlayerColor;
		public TextView txtPlayerName;
	}
}
