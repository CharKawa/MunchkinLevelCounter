package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayer;

/**
 * Created by G_Art on 5/8/2014.
 */
public class ContinueDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle(R.string.title_dialog_continue)
                .setCancelable(false)
                .setMessage(R.string.message_for_dialog_cont)
                .setPositiveButton(R.string.ok_btn_for_dialog_cont, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentPlayer fragment = (FragmentPlayer) getActivity().getFragmentManager().findFragmentById(R.id.currentPlayer_Fragment);
                        fragment.doPositiveClickContinueDialog();
                    }
                })
                .setNegativeButton(R.string.cancel_btn_for_dialog_cont, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentPlayer fragment = (FragmentPlayer) getActivity().getFragmentManager().findFragmentById(R.id.currentPlayer_Fragment);
                        fragment.doNegativeClickContinueDialog();
                    }
                });

        return builder.create();

    }
}
