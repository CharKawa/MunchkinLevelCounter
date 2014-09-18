package com.g_art.munchkinlevelcounter.listadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.bean.Player;

import java.util.ArrayList;

/**
 * Created by G_Art on 27/7/2014.
 */
public class InGameListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Player player = null;
    private ArrayList data;
    private Activity activity;


    public InGameListAdapter(Activity activity, ArrayList arrayList) {
        this.activity = activity;
        this.data = arrayList;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) this.activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (data.size() <= 0) {
            return 1;
        } else {
            return data.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * ****** Holder Class to contain inflated xml file elements ********
     */
    public static class ViewHolder {
        public TextView txtPlayerName;
        public TextView txtPlayerLvl;
        public TextView txtPlayerLvlValue;
        public TextView txtPlayerTotal;
        public TextView txtPlayerTotalValue;
    }

    /**
     * *** Depends upon data size called for each row , Create each ListView row ****
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        final int playerPosition = position;

        if (convertView == null) {
            view = inflater.inflate(R.layout.in_game_players_list, null);

            holder = new ViewHolder();
            holder.txtPlayerName = (TextView) view.findViewById(R.id.InGameListPlayerName);
            holder.txtPlayerLvl = (TextView) view.findViewById(R.id.InGameListPlayerLvL);
            holder.txtPlayerLvlValue = (TextView) view.findViewById(R.id.InGameListPlayerLvLValue);
            holder.txtPlayerTotal = (TextView) view.findViewById(R.id.InGameListPlayerTotal);
            holder.txtPlayerTotalValue = (TextView) view.findViewById(R.id.InGameListPlayerTotalValue);

            /************  Set holder with LayoutInflater ************/
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (data.size() <= 0) {

        } else {
            /***** Get each Model object from Arraylist ********/
            player = null;
            player = (Player) data.get(playerPosition);

            /************  Set Model values in Holder elements ***********/
            holder.txtPlayerName.setText(player.getName());
            if (holder.txtPlayerTotalValue != null) {
                holder.txtPlayerLvlValue.setText(Integer.toString(player.getLevel()));
                int totalPower = player.getGear() + player.getLevel();
                holder.txtPlayerTotalValue.setText(Integer.toString(totalPower));
            }

        }

        return view;
    }
}
