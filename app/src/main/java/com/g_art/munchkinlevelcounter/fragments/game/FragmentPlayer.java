package com.g_art.munchkinlevelcounter.fragments.game;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.GameActivity;
import com.g_art.munchkinlevelcounter.activity.Stats;
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.dialog.ContinueDialog;
import com.g_art.munchkinlevelcounter.fragments.dialog.DiceDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlayer extends Fragment implements View.OnClickListener {

    public static final String DICE_KEY = "dice_key";
    final String TAG = "GameActivity_Munchkin_Test";
    private final String PLAYER_KEY = "playersList";
    private final String BUNDLE_STATS_KEY = "bundleStats";
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
        holder.btnRollDice = (ImageButton) view.findViewById(R.id.btnDice);
        holder.btnRollDice.setOnClickListener(this);
        holder.btnFinish = (ImageButton) view.findViewById(R.id.btnFinish);
        holder.btnFinish.setOnClickListener(this);
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
                if (selectedPlayer.getGear() != MIN_STAT) {
                    selectedPlayer.setGear(selectedPlayer.getGear() - 1);
                }
                break;

            case R.id.btnDice:
                rollADice();
                break;

            case R.id.btnFinish:
                mCallback.savePlayersStats();
                openStatsActivity();
                break;

            case R.id.btnNextPlayer:
                if (mCallback.savePlayersStats()) {
                    if (!mCallback.onNextTurnClick(selectedPlayer)) {
                        Toast.makeText(getActivity(), getString(R.string.error_next_turn), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_save_players), Toast.LENGTH_LONG).show();
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
        holder.btnFinish.setImageResource(R.drawable.munchkin_in_game_end);
    }

    public void doNegativeClickContinueDialog() {
        selectedPlayer.setWinner(true);
        selectedPlayer.setLevel(selectedPlayer.getLevel() + 1);
        onClick(view.findViewById(R.id.btnFinish));
        openStatsActivity();
    }

    private void dismissDialog() {
        continueDialog = null;
    }

    private void openStatsActivity() {
        Intent intent = new Intent(getActivity(), Stats.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PLAYER_KEY, ((GameActivity) getActivity()).getPlayersList());
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

        mCallback.savePlayersStats();
    }

    private void setSelectedPlayer() {
        holder.txtCurrentPlayerName.setText(selectedPlayer.getName());
        holder.txtCurrentPlayerLvl.setText(Integer.toString(selectedPlayer.getLevel()));
        holder.txtCurrentPlayerGear.setText(Integer.toString(selectedPlayer.getGear()));
        holder.txtCurrentPlayerPower.setText(Integer.toString(selectedPlayer.getGear() + selectedPlayer.getLevel()));
    }

    private void rollADice() {
        int resId = 1;
        int Min = 1;
        int Max = 6;
        int dice = Min + (int) (Math.random() * ((Max - Min) + 1));
        switch (dice) {
            case 1:
                resId = R.drawable.dice_1;
                break;
            case 2:
                resId = R.drawable.dice_2;
                break;
            case 3:
                resId = R.drawable.dice_3;
                break;
            case 4:
                resId = R.drawable.dice_4;
                break;
            case 5:
                resId = R.drawable.dice_5;
                break;
            case 6:
                resId = R.drawable.dice_6;
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(DICE_KEY, resId);
        DiceDialog diceDialog = new DiceDialog();
        diceDialog.setArguments(bundle);
        diceDialog.show(getActivity().getFragmentManager(), "dice");
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
        public ImageButton btnLvlUp;
        public ImageButton btnLvlDwn;
        public ImageButton btnGearUp;
        public ImageButton btnGearDwn;
        public ImageButton btnRollDice;
        public ImageButton btnFinish;
        public Button btnNextPlayer;
    }
}
