package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.NewPlayers;
import com.g_art.munchkinlevelcounter.model.Sex;

/**
 * Created by G_Art on 21/7/2014.
 */
public class PlayerNameDialog extends DialogFragment implements View.OnClickListener {

    private EditText playerName;
    private ImageButton btnPlayerSex;
    private Sex playerSex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.new_player_dialog, null);

        builder.setView(v);

        playerName = (EditText) v.findViewById(R.id.playerName);
        btnPlayerSex = (ImageButton) v.findViewById(R.id.new_player_sex);

        playerSex = (Sex) getArguments().getSerializable(NewPlayers.PLAYER_SEX);
        updateBtnSex();

        btnPlayerSex.setOnClickListener(this);

        playerName.setText(getArguments().getString(NewPlayers.PLAYER_NAME));
        playerName.requestFocus();

        builder.setTitle(R.string.title_dialog_new_Player)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = playerName.getText().toString();
                        if (name.isEmpty()) {
                            Toast.makeText(getActivity(), getString(R.string.error_empty_name), Toast.LENGTH_SHORT).show();
                        } else {
                            ((NewPlayers) getActivity()).doPositiveClickPlayerNameDialog(name, playerSex);
                        }

                    }
                })
                .setNegativeButton(R.string.dialog_cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((NewPlayers) getActivity()).doNegativeClickPlayerNameDialog();
                    }
                });

        return builder.create();

    }

    @Override
    public void onResume() {
        super.onResume();

        playerName.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(playerName, 0);
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.new_player_sex :
                if (playerSex == Sex.MAN) {
                    playerSex = Sex.WOMAN;
                } else {
                    playerSex = Sex.MAN;
                }
                updateBtnSex();
                break;
        }
    }

    private void updateBtnSex() {
        if (playerSex == null) {
            playerSex = Sex.MAN;
        }
        if (playerSex == Sex.MAN) {
            btnPlayerSex.setImageResource(R.drawable.man);
        } else {
            btnPlayerSex.setImageResource(R.drawable.woman);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
