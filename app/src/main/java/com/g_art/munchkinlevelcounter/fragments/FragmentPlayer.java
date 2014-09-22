package com.g_art.munchkinlevelcounter.fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.GameActivity;
import com.g_art.munchkinlevelcounter.activity.Stats;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.dialog.ContinueDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlayer extends Fragment implements View.OnClickListener {

    private ViewHolder holder;
    private Player selectedPlayer;
    private final String PLAYER_KEY = "playersList";
    private final String STATS_KEY = "collectStats";
    private final String BUNDLE_STATS_KEY = "bundleStats";
    private final int MAX_LVL = 10;
    private final int MIN_STAT = 0;
    private View view;
    private boolean contAfterMaxLVL = false;
    final String TAG = "GameActivity_Munchkin_Test";
    private PlayersUpdate mCallback;
    private static boolean showStatsMessage = false;


    public FragmentPlayer() {
        // Required empty public constructor
    }

    // interface for communication between fragments
    public interface PlayersUpdate {

        public void onPlayersUpdate();

        public boolean savePlayersStats();

        public boolean onNextTurnClick(Player player);

        public boolean collectStats();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (PlayersUpdate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public class ViewHolder {
        public TextView txtCurrentPlayerName;
        public TextView txtCurrentPlayerPower;
        public TextView txtCurrentPlayerLvl;
        public TextView txtCurrentPlayerGear;
        public ImageButton btnLvlUp;
        public ImageButton btnLvlDwn;
        public ImageButton btnGearUp;
        public ImageButton btnGearDwn;
        public ImageButton btnRollDice;
        public ImageButton btnNextTurn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_player, container, false);

        holder = new ViewHolder();
        holder.txtCurrentPlayerName = (TextView) view.findViewById(R.id.currentPlayer);
        holder.txtCurrentPlayerLvl = (TextView) view.findViewById(R.id.txtPlayerLvl);
        holder.txtCurrentPlayerGear = (TextView) view.findViewById(R.id.txtPlayerGear);
        holder.txtCurrentPlayerPower = (TextView) view.findViewById(R.id.total);
        holder.btnLvlUp = (ImageButton) view.findViewById(R.id.btnLvlUp);
        holder.btnLvlUp.setOnClickListener(this);
        holder.btnLvlDwn = (ImageButton) view.findViewById(R.id.btnLvlDwn);
        holder.btnLvlDwn.setOnClickListener(this);
        holder.btnGearUp = (ImageButton) view.findViewById(R.id.btnGearUp);
        holder.btnGearUp.setOnClickListener(this);
        holder.btnGearDwn = (ImageButton) view.findViewById(R.id.btnGearDwn);
        holder.btnGearDwn.setOnClickListener(this);
        holder.btnRollDice = (ImageButton) view.findViewById(R.id.btnDice);
        holder.btnRollDice.setOnClickListener(this);
        holder.btnNextTurn = (ImageButton) view.findViewById(R.id.btnNextTurn);
        holder.btnNextTurn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        if (!mCallback.collectStats()) {
//            holder.btnNextTurn.setEnabled(false);
            holder.btnNextTurn.setActivated(false);
        } else {
            holder.btnNextTurn.setActivated(true);
//            holder.btnNextTurn.setEnabled(true);
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLvlUp:
                if (!isMaxLvlReached(selectedPlayer.getLevel())) {
                    selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
                } else {
                    if (contAfterMaxLVL) {
                        selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
                    } else {
                        DialogFragment continueDialog = new ContinueDialog();
                        continueDialog.show(getActivity().getFragmentManager(), "continueDialog");
                        if (contAfterMaxLVL) {
                            selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
                        } else {
                            //endGame
                        }
                    }
                }
                break;

            case R.id.btnLvlDwn:
                if (selectedPlayer.getLevel() != MIN_STAT + 1) {
                    selectedPlayer.setLevel(selectedPlayer.getLevel() - 1);
                }
                break;

            case R.id.btnGearUp:
                selectedPlayer.setGear(selectedPlayer.getGear() + 1);
                break;

            case R.id.btnGearDwn:
                if (selectedPlayer.getGear() != MIN_STAT) {
                    selectedPlayer.setGear(selectedPlayer.getGear() - 1);
                }
                break;

            case R.id.btnDice:
                rollADice();
                break;

            case R.id.btnNextTurn:
                if (contAfterMaxLVL) {
                    mCallback.savePlayersStats();
                    openStatsActivity();
                } else {
                    if (holder.btnNextTurn.isActivated()) {
                        if (mCallback.savePlayersStats()) {
                            if (mCallback.onNextTurnClick(selectedPlayer)) {
                            } else {
                                Toast.makeText(getActivity(), "Error in the next turn!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "It was error while trying to save players!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (!showStatsMessage) {
                            Toast.makeText(getActivity(), "Statistic is off, now it's just switch players", Toast.LENGTH_SHORT).show();
                            showStatsMessage = true;
                        }
                        mCallback.onNextTurnClick(selectedPlayer);
                    }
                }
                break;
        }
        setSelectedPlayer();
        mCallback.onPlayersUpdate();
    }

    public void doPositiveClickContinueDialog() {
        contAfterMaxLVL = true;
        selectedPlayer.setWinner(true);
        onClick(view.findViewById(R.id.btnLvlUp));
        holder.btnNextTurn.setImageResource(R.drawable.munchkin_in_game_end);
    }

    public void doNegativeClickContinueDialog() {
        selectedPlayer.setWinner(true);
        selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
        onClick(view.findViewById(R.id.btnNextTurn));
        openStatsActivity();
    }

    private void openStatsActivity() {
        Intent intent = new Intent(getActivity(), Stats.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PLAYER_KEY, ((GameActivity) getActivity()).getPlayersList());
        bundle.putBoolean(STATS_KEY, mCallback.collectStats());
        intent.putExtra(BUNDLE_STATS_KEY, bundle);
        startActivity(intent);
    }

    private boolean isMaxLvlReached(int currentLvl) {
        boolean maxLvlReached = false;
        if (currentLvl + 1 == MAX_LVL) {
            maxLvlReached = true;
        }
        return maxLvlReached;
    }

    public void changeSelectedPlayer(Player player) {
        selectedPlayer = player;
        setSelectedPlayer();

        if (mCallback.collectStats()) {
            mCallback.savePlayersStats();
        }
    }

    private void setSelectedPlayer() {
        holder.txtCurrentPlayerName.setText(selectedPlayer.getName());
        holder.txtCurrentPlayerLvl.setText(Integer.toString(selectedPlayer.getLevel()));
        holder.txtCurrentPlayerGear.setText(Integer.toString(selectedPlayer.getGear()));
        holder.txtCurrentPlayerPower.setText(Integer.toString(selectedPlayer.getGear() + selectedPlayer.getLevel()));
    }

    private void rollADice() {
        int Min = 1;
        int Max = 6;
        int dice = Min + (int) (Math.random() * ((Max - Min) + 1));
        Toast.makeText(getActivity(), String.valueOf(dice), Toast.LENGTH_SHORT).show();
    }
}