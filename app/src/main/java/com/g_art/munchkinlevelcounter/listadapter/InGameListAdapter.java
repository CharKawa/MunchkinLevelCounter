package com.g_art.munchkinlevelcounter.listadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by G_Art on 27/7/2014.
 */
public class InGameListAdapter extends BaseAdapter {

    public static class ViewHolder{
        public TextView txtPlayerName;
        public TextView txtlvl;
        public TextView txtPlayerLvl;
        public TextView txtTotal;
        public TextView txtPlayerTotal;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
