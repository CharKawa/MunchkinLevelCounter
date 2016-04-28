package com.g_art.munchkinlevelcounter.listadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.NewPlayers;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.bean.Sex;

import java.util.ArrayList;

/**
 * Created by G_Art on 16/7/2014.
 */
public class CustomListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    private ArrayList data;
    private int i = 0;

    public CustomListAdapter(Activity a, ArrayList d) {

        /********** Take passed values **********/
        activity = a;
        data = d;


        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
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
     * *** Depends upon data size called for each row , Create each ListView row ****
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        final int playerPosition = position;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.list_players_for_new_game, null);

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.newPlayerName);
            holder.imEdit = (ImageButton) vi.findViewById(R.id.imBtnEdit);
            holder.imBtnDel = (ImageButton) vi.findViewById(R.id.imBtnDel);
            holder.imvPlayerSex = (ImageView) vi.findViewById(R.id.imvPlayerSex);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.text.setText(R.string.no_players);
            holder.imBtnDel.setVisibility(View.GONE);
            holder.imEdit.setVisibility(View.GONE);
            holder.imvPlayerSex.setVisibility(View.GONE);

        } else {
            /***** Get each Model object from Arraylist ********/
            Player player = null;
            player = (Player) data.get(playerPosition);

            /************  Set Model values in Holder elements ***********/

            holder.text.setText(player.getName());

            holder.imEdit.setVisibility(View.VISIBLE);
            holder.imEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewPlayers sct = (NewPlayers) activity;
                    sct.playerEdit(playerPosition);
                }
            });

            holder.imBtnDel.setVisibility(View.VISIBLE);
            holder.imBtnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewPlayers sct = (NewPlayers) activity;
                    sct.playerDelete(playerPosition);
                }
            });

            holder.imvPlayerSex.setVisibility(View.VISIBLE);

            if (player.getSex() == Sex.MAN) {
                holder.imvPlayerSex.setImageResource(R.drawable.man);
            } else {
                holder.imvPlayerSex.setImageResource(R.drawable.woman);
            }


        }
        return vi;
    }

    /**
     * ****** Holder Class to contain inflated xml file elements ********
     */
    public static class ViewHolder {

        public TextView text;
        public ImageButton imEdit;
        public ImageButton imBtnDel;
        public ImageView imvPlayerSex;

    }

}
