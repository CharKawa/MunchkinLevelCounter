package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.NewPlayers;

/**
 * Created by G_Art on 21/7/2014.
 */
public class NewPlayerDialog extends DialogFragment {

    final String TAG = "Munchkin";


    private EditText playerName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.new_player_dialog, null);

        builder.setView(v);

        playerName = (EditText) v.findViewById(R.id.playerName);

        builder.setTitle(R.string.title_dialog_new_Player)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = playerName.getText().toString();
                        Log.d(TAG, "Player name: " + name);
                        ((NewPlayers) getActivity()).doPositiveClickNewPlayerDialog(name);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((NewPlayers) getActivity()).doNegativeClickNewPlayerDialog();
                    }
                });

        return builder.create();

    }

}
