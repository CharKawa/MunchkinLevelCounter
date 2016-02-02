package com.g_art.munchkinlevelcounter.fragments.info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.NewPlayers;

/**
 * LevelCounter
 * Created by G_Art on 1/12/2015.
 */
public class ThanksPopup extends DialogFragment implements View.OnClickListener {

    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_tanks_popup, container, false);
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_tanks_popup, null);

        builder.setView(v);


        builder.setTitle(R.string.app_name)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_showLater, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.btn_donate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((NewPlayers) getActivity()).doNegativeClickPlayerNameDialog();
                    }
                })
                .setNeutralButton(R.string.btn_neverAsk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((NewPlayers) getActivity()).doNegativeClickPlayerNameDialog();
                    }
                });

        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onClick(View v) {

    }
}
