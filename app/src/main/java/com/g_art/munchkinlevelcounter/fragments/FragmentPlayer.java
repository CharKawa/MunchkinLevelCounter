package com.g_art.munchkinlevelcounter.fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlayer extends Fragment implements View.OnClickListener {

    ViewHolder holder;
    private Player selectedPlayer;
    private static final String PLAYER_KEY = "playersList";
    private final int MAX_LVL = 10;
    private final int MIN_STAT = 0;
    private View view;
    private boolean contAfterMaxLVL = false;
    //    final String TAG = "Munchkin";
    final String TAG = "GameActivity_Munchkin_Test";
    private PlayersUpdate mCallback;


    public FragmentPlayer() {
        // Required empty public constructor
    }

    // interface for communication between fragments
    public interface PlayersUpdate {
        public void onPlayersUpdate();

        public boolean savePlayersStats();
        public boolean onNextTurnClick(Player player);
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

    public static class ViewHolder {
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
        view.setTag(holder);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLvlUp:
                Log.d(TAG, "btnLvlUp clicked");
                if (!isMaxLvlReached(selectedPlayer.getLevel())) {
                    selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
                } else {
                    if (contAfterMaxLVL) {
                        selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
                    } else {
                        DialogFragment continueDialog = new ContinueDialog();
                        continueDialog.show(getActivity().getFragmentManager(), "dialog");
                        if (contAfterMaxLVL) {
                            selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
                            Log.d(TAG, "Continue the game");
                        } else {
                            Log.d(TAG, "End the game");
                        }
                    }
                }
                break;

            case R.id.btnLvlDwn:
                Log.d(TAG, "btnLvlDwn clicked");
                if (selectedPlayer.getLevel() != MIN_STAT + 1) {
                    selectedPlayer.setLevel(selectedPlayer.getLevel() - 1);
                }
                break;

            case R.id.btnGearUp:
                selectedPlayer.setGear(selectedPlayer.getGear() + 1);
                Log.d(TAG, "btnGearUp clicked");
                break;

            case R.id.btnGearDwn:
                Log.d(TAG, "btnGearDwn clicked");
                if (selectedPlayer.getGear() != MIN_STAT) {
                    selectedPlayer.setGear(selectedPlayer.getGear() - 1);
                }
                break;

            case R.id.btnDice:
                rollADice();
                break;

            case R.id.btnNextTurn:
                Log.d(TAG, "btnNextTurn clicked");
                if (mCallback.savePlayersStats()) {
                    if (mCallback.onNextTurnClick(selectedPlayer)) {
                        Toast.makeText(getActivity(), " Data saved correctly! ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error in the next turn!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "It was error while trying to save players!", Toast.LENGTH_LONG).show();
                }
                break;
        }
        setSelectedPlayer();
        mCallback.onPlayersUpdate();
    }

    public void doPositiveClick() {
        contAfterMaxLVL = true;
        onClick(view.findViewById(R.id.btnLvlUp));
        Log.d(TAG, "Continue the game");
    }

    public void doNegativeClick() {
        selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
        onClick(view.findViewById(R.id.btnNextTurn));
        Intent intent = new Intent(getActivity(), Stats.class);
        intent.putParcelableArrayListExtra(PLAYER_KEY, ((GameActivity) getActivity()).getPlayersList());
        startActivity(intent);
        Log.d(TAG, "Negative click!");
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
        Log.d(TAG, "Getting the position of the player: " + selectedPlayer);
        setSelectedPlayer();
    }

    private void setSelectedPlayer() {
        holder.txtCurrentPlayerName.setText(selectedPlayer.getName());
        holder.txtCurrentPlayerLvl.setText(Integer.toString(selectedPlayer.getLevel()));
        holder.txtCurrentPlayerGear.setText(Integer.toString(selectedPlayer.getGear()));
        holder.txtCurrentPlayerPower.setText(Integer.toString(selectedPlayer.getGear() + selectedPlayer.getLevel()));
        Log.d(TAG, "Setting player");
    }

    private void rollADice() {
        Log.d(TAG, "btnDice clicked");
        int Min = 1;
        int Max = 6;
        int dice = Min + (int) (Math.random() * ((Max - Min) + 1));
        Log.d("DICE", String.valueOf(dice));
//                Toast.makeText(getActivity(),String.valueOf(dice),Toast.LENGTH_SHORT).show();
    }


}
