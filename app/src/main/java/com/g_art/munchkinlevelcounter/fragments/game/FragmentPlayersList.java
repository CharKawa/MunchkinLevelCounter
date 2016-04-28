package com.g_art.munchkinlevelcounter.fragments.game;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.ArrayList;

public class FragmentPlayersList extends Fragment implements AdapterView.OnItemClickListener {

    private static final String PLAYER_KEY = "playersList";
    private ArrayList<Player> playersList;
    private View view;
    private View currentlySelected;
    private Boolean firstTimeStartup = true;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView inGamePlayersList;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private InGameListAdapter inGameAdapter;
    private OnPlayerSelectedListener mCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentPlayersList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        if (getArguments() != null) {
            playersList = getArguments().getParcelableArrayList(PLAYER_KEY);
        }
        inGameAdapter = new InGameListAdapter(getActivity(), playersList);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPlayerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlayerSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PLAYER_KEY, playersList);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_players_list, container, false);


        inGamePlayersList = (ListView) view.findViewById(R.id.inGamePlayersList);
        inGamePlayersList.setAdapter(inGameAdapter);
        inGamePlayersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        inGamePlayersList.setOnItemClickListener(this);

        return view;
    }

    public void scrollToPlayer(int position) {
        smoothScrollToPositionFromTop(inGamePlayersList, position);

        View child = getChildAtPosition(inGamePlayersList, position);
        if (child != null) {
            processRow(inGamePlayersList, child, position);
        }
    }

    public void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        if (currentlySelected != null && currentlySelected != child) {
            unhighlightSelection(currentlySelected);
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
            }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });

    }

    public View getChildAtPosition(final AdapterView view, final int position) {
        if (null == view) {
            return null;
        }
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        processRow(parent, view, position);

        mCallback.onPlayerSelected(position);
    }

    private void processRow(AdapterView<?> parent, View view, int position) {
        if (firstTimeStartup) {// first time  highlight first row
            currentlySelected = parent.getChildAt(0);
        }
        firstTimeStartup = false;

        if (null != currentlySelected && currentlySelected != view) {
            unhighlightSelection(currentlySelected);
        }

        currentlySelected = view;
        highlightSelection(currentlySelected);
    }


    public void listUpdate() {
        inGameAdapter.notifyDataSetChanged();
        highlightSelection(currentlySelected);

    }

    public void highlightSelection(View rowView) {
        rowView.setBackgroundColor(getResources().getColor(R.color.list_selected));

        TextView name = (TextView) rowView.findViewById(R.id.InGameListPlayerName);
        name.setTextColor(getResources().getColor(R.color.actionBarText_color));
        TextView lvl = (TextView) rowView.findViewById(R.id.InGameListPlayerLvL);
        lvl.setTextColor(getResources().getColor(R.color.actionBarText_color));
        TextView lvlValue = (TextView) rowView.findViewById(R.id.InGameListPlayerLvLValue);
        lvlValue.setTextColor(getResources().getColor(R.color.actionBarText_color));
        TextView total = (TextView) rowView.findViewById(R.id.InGameListPlayerTotal);
        total.setTextColor(getResources().getColor(R.color.actionBarText_color));
        TextView totalValue = (TextView) rowView.findViewById(R.id.InGameListPlayerTotalValue);
        totalValue.setTextColor(getResources().getColor(R.color.actionBarText_color));
    }

    public void unhighlightSelection(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);

        TextView name = (TextView) rowView.findViewById(R.id.InGameListPlayerName);
        name.setTextColor(getResources().getColor(R.color.text_color));
        TextView lvl = (TextView) rowView.findViewById(R.id.InGameListPlayerLvL);
        lvl.setTextColor(getResources().getColor(R.color.text_color));
        TextView lvlValue = (TextView) rowView.findViewById(R.id.InGameListPlayerLvLValue);
        lvlValue.setTextColor(getResources().getColor(R.color.text_color));
        TextView total = (TextView) rowView.findViewById(R.id.InGameListPlayerTotal);
        total.setTextColor(getResources().getColor(R.color.text_color));
        TextView totalValue = (TextView) rowView.findViewById(R.id.InGameListPlayerTotalValue);
        totalValue.setTextColor(getResources().getColor(R.color.text_color));
    }

    // interface for communication between fragments
    public interface OnPlayerSelectedListener {
        void onPlayerSelected(int position);
    }

    public class InGameListAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;
        private ArrayList data;


        public InGameListAdapter(Activity activity, ArrayList arrayList) {
            this.data = arrayList;

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

            View view = convertView;
            ViewHolder holder;

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

            if (firstTimeStartup && position == 0) {
                currentlySelected = view;
                highlightSelection(view);
                firstTimeStartup = false;
            } else {
                if (currentlySelected != view) {
                    unhighlightSelection(view);
                }
            }

            if (data.size() > 0) {
                /***** Get each Model object from Arraylist ********/
                Player player = (Player) data.get(position);

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

        /**
         * ****** Holder Class to contain inflated xml file elements ********
         */

    }

    class ViewHolder {
        public TextView txtPlayerName;
        public TextView txtPlayerLvl;
        public TextView txtPlayerLvlValue;
        public TextView txtPlayerTotal;
        public TextView txtPlayerTotalValue;
    }

}