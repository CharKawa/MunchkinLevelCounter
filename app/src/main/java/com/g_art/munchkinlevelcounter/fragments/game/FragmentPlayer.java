package com.g_art.munchkinlevelcounter.fragments.game;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.model.Sex;
import com.g_art.munchkinlevelcounter.fragments.dialog.ContinueDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlayer extends Fragment implements View.OnClickListener {

    private final int MIN_STAT = 0;
    private ViewHolder holder;
    private DialogFragment continueDialog;
    private Player selectedPlayer;
    private int MAX_LVL;
    private View view;
    private boolean contAfterMaxLVL = false;
    private PlayersUpdate mCallback;

    public FragmentPlayer() {
        // Required empty public constructor
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
        holder.btnSexType = (ImageButton) view.findViewById(R.id.player_sex);
        holder.btnSexType.setOnClickListener(this);
        holder.btnNextPlayer = (Button) view.findViewById(R.id.btnNextPlayer);
        holder.btnNextPlayer.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        MAX_LVL = mCallback.maxLvl();
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
                        continueDialog = new ContinueDialog();
                        continueDialog.show(getActivity().getFragmentManager(), "continueDialog");
                        if (contAfterMaxLVL) {
                            selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
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
                    selectedPlayer.setGear(selectedPlayer.getGear() - 1);
                break;

            case R.id.btnNextPlayer:
                if (!mCallback.onNextTurnClick(selectedPlayer)) {
                    Toast.makeText(getActivity(), getString(R.string.error_next_turn), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.player_sex:
                if (selectedPlayer.getSex() == Sex.MAN) {
                    selectedPlayer.setSex(Sex.WOMAN);
                } else {
                    selectedPlayer.setSex(Sex.MAN);
                }
                updatePlayerSex();
                break;
        }
        setSelectedPlayer();
        mCallback.onPlayersUpdate();
    }

    public void doPositiveClickContinueDialog() {
        contAfterMaxLVL = true;
        selectedPlayer.setWinner(true);
        onClick(view.findViewById(R.id.btnLvlUp));
    }

    public void doNegativeClickContinueDialog() {
        selectedPlayer.setWinner(true);
        selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
        mCallback.finishClick();
    }

    private void dismissDialog() {
        continueDialog = null;
    }

    private boolean isMaxLvlReached(int currentLvl) {
        boolean maxLvlReached = false;
        MAX_LVL = mCallback.maxLvl();
        if (currentLvl + 1 == MAX_LVL) {
            maxLvlReached = true;
        }
        return maxLvlReached;
    }

    public void changeSelectedPlayer(Player player) {
        selectedPlayer = player;
        setSelectedPlayer();

        mCallback.savePlayersStats();
    }

    private void setSelectedPlayer() {
        if (selectedPlayer != null) {
            holder.txtCurrentPlayerName.setText(selectedPlayer.getName());
            holder.txtCurrentPlayerLvl.setText(Integer.toString(selectedPlayer.getLevel()));
            holder.txtCurrentPlayerGear.setText(Integer.toString(selectedPlayer.getGear()));
            holder.txtCurrentPlayerPower.setText(Integer.toString(selectedPlayer.getGear() + selectedPlayer.getLevel()));
            updatePlayerSex();
        }
    }

    private void updatePlayerSex() {
        if (Sex.MAN == selectedPlayer.getSex()) {
            holder.btnSexType.setImageResource(R.drawable.man);
        } else {
            holder.btnSexType.setImageResource(R.drawable.woman);
        }
    }


    @Override
    public void onDestroy() {
        if (continueDialog != null) {
            dismissDialog();
        }
        super.onDestroy();
    }

    // interface for communication between fragments
    public interface PlayersUpdate {

        void finishClick();

        void onPlayersUpdate();

        boolean savePlayersStats();

        boolean onNextTurnClick(Player player);

        int maxLvl();
    }

    public class ViewHolder {
        public TextView txtCurrentPlayerName;
        public TextView txtCurrentPlayerPower;
        public TextView txtCurrentPlayerLvl;
        public TextView txtCurrentPlayerGear;
        public ImageButton btnSexType;
        public ImageButton btnLvlUp;
        public ImageButton btnLvlDwn;
        public ImageButton btnGearUp;
        public ImageButton btnGearDwn;
        public Button btnNextPlayer;
    }
}
