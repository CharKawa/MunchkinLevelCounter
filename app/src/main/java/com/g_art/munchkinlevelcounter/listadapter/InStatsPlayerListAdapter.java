package com.g_art.munchkinlevelcounter.listadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.fragments.LineFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by G_Art on 2/9/2014.
 */
public class InStatsPlayerListAdapter extends BaseAdapter {

    private Activity activity;
    private HashMap playersMap;
    private final ArrayList mData;
    private static LayoutInflater inflater = null;


    public static class ViewHolder {
        public View viewPlayerColor;
        public TextView txtPlayerName;
    }

    public InStatsPlayerListAdapter(Activity activity, HashMap playersMap) {

        this.activity = activity;
        this.playersMap = playersMap;
        mData = new ArrayList();

        if (!playersMap.isEmpty()) {
            mData.addAll(playersMap.entrySet());
        }

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) this.activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (playersMap.size() <= 0) {
            return 1;
        } else {
            return playersMap.size();
        }
    }

    @Override
    public Map.Entry<Integer, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        final int playerPosition = position;

        if (convertView == null) {
            view = inflater.inflate(R.layout.in_stat_player_list, null);

            holder = new ViewHolder();
            holder.txtPlayerName = (TextView) view.findViewById(R.id.txtPlayerNameInStats);
            holder.viewPlayerColor = view.findViewById(R.id.playerColor);

            /************  Set holder with LayoutInflater ************/
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (playersMap.size() <= 0) {
            holder.txtPlayerName.setText(LineFragment.PREFS_NO_DATA);
        } else {
            Map.Entry<Integer, String> item = getItem(playerPosition);
            holder.viewPlayerColor.setBackgroundColor(item.getKey());
            holder.txtPlayerName.setText(item.getValue());
        }

        return view;
    }
}
