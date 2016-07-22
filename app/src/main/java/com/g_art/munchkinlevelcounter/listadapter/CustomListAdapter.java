package com.g_art.munchkinlevelcounter.listadapter;

import android.app.Activity;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.NewPlayers;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.model.Sex;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

/**
 * Created by G_Art on 16/7/2014.
 */
public class CustomListAdapter extends DragItemAdapter<Pair<Long, Player>, CustomListAdapter.ViewHolder> {

	private final int mLayoutId;
	private final int mGrabHandleId;
	private Activity mActivity;
	private ArrayList<Pair<Long, Player>> mList;

	public CustomListAdapter(ArrayList<Pair<Long, Player>> list, int layoutId, int grabHandleId,
							 boolean dragOnLongPress, Activity activity) {
		super(dragOnLongPress);
		mLayoutId = layoutId;
		mGrabHandleId = grabHandleId;
		this.mActivity = activity;
		setHasStableIds(true);
		setItemList(list);
		this.mList = list;
	}

	@Override
	public long getItemId(int position) {
		return mItemList.get(position).first;
	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);
		Player player = mItemList.get(position).second;
		setPlayer(holder, player, position);
	}

	private void setPlayer(ViewHolder holder, Player player, final int playerPosition) {
		if (mList.isEmpty()) {
			holder.text.setText(R.string.no_players);
			holder.imBtnDel.setVisibility(View.GONE);
			holder.imEdit.setVisibility(View.GONE);
			holder.imBtnPlayerSex.setVisibility(View.GONE);

		} else {
			/***** Get each Model object from Arraylist ********/

			/************  Set Model values in Holder elements ***********/

			holder.text.setText(player.getName());

			holder.imEdit.setVisibility(View.VISIBLE);
			holder.imEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NewPlayers sct = (NewPlayers) mActivity;
					sct.playerEdit(playerPosition);
				}
			});

			holder.imBtnDel.setVisibility(View.VISIBLE);
			holder.imBtnDel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NewPlayers sct = (NewPlayers) mActivity;
					sct.playerDelete(playerPosition);
				}
			});

			holder.imBtnPlayerSex.setVisibility(View.VISIBLE);
			holder.imBtnPlayerSex.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NewPlayers sct = (NewPlayers) mActivity;
					sct.playerChangeSex(playerPosition);
				}
			});

			if (player.getSex() == Sex.MAN) {
				holder.imBtnPlayerSex.setImageResource(R.drawable.man);
			} else {
				holder.imBtnPlayerSex.setImageResource(R.drawable.woman);
			}
		}
	}

	/**
	 * *** Depends upon data size called for each row , Create each ListView row ****
	 */
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View vi = convertView;
//        ViewHolder holder;
//        final int playerPosition = position;
//
//        if (convertView == null) {
//
//            vi = inflater.inflate(R.layout.list_players_for_new_game, null);
////
//            holder = new ViewHolder(null, 0);
////            holder.text = (TextView) vi.findViewById(R.id.newPlayerName);
////            holder.imEdit = (ImageButton) vi.findViewById(R.id.imBtnEdit);
////            holder.imBtnDel = (ImageButton) vi.findViewById(R.id.imBtnDel);
////            holder.imBtnPlayerSex = (ImageButton) vi.findViewById(R.id.imvPlayerSex);
//
//            /************  Set holder with LayoutInflater ************/
////            vi.setTag(holder);
//        } else
//            holder = (ViewHolder) vi.getTag();
//
//
//
//
//        }
//        return vi;
//    }

	/**
	 * ****** Holder Class to contain inflated xml file elements ********
	 */
	public class ViewHolder extends DragItemAdapter<Pair<Long, Player>, CustomListAdapter.ViewHolder>.ViewHolder {

		public TextView text;
		public ImageButton imEdit;
		public ImageButton imBtnDel;
		public ImageButton imBtnPlayerSex;

		public ViewHolder(View itemView) {
			super(itemView, mGrabHandleId);

			text = (TextView) itemView.findViewById(R.id.newPlayerName);
			imEdit = (ImageButton) itemView.findViewById(R.id.imBtnEdit);
			imBtnDel = (ImageButton) itemView.findViewById(R.id.imBtnDel);
			imBtnPlayerSex = (ImageButton) itemView.findViewById(R.id.imvPlayerSex);
		}
	}

}
