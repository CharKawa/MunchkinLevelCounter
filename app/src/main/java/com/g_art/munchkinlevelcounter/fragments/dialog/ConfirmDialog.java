package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.GameActivity;

/**
 * Created by G_Art on 4/9/2014.
 */
public class ConfirmDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_dialog_confirm)
                .setCancelable(false)
                .setMessage(R.string.message_for_dialog_confirm)
                .setPositiveButton(R.string.ok_btn_for_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity gameActivity = (GameActivity) getActivity();
                        gameActivity.onNegativeClickConfirmDialog();
                    }
                })
                .setNegativeButton(R.string.cancel_btn_for_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity gameActivity = (GameActivity) getActivity();
                        gameActivity.onPositiveClickConfirmDialog();
                    }
                });
        return super.onCreateDialog(savedInstanceState);
    }
}
