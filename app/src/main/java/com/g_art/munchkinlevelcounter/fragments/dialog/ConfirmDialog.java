package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.g_art.munchkinlevelcounter.activity.GameActivity;

/**
 * Created by G_Art on 4/9/2014.
 */
public class ConfirmDialog extends DialogFragment {

    public static final String SOURCE_KEY = "source_key";
    public static final String REQUEST_KEY = "request_key";
    public static final String RESULT_KEY = "result_key";
    public static final String TITLE_KEY = "title_key";
    public static final String MSG_KEY = "msg_key";
    public static final String OK_KEY = "ok_key";
    public static final String NOT_KEY = "not_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.alertDialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle bundle = getArguments();

        builder.setTitle(bundle.getInt(TITLE_KEY))
                .setCancelable(false)
                .setMessage(bundle.getInt(MSG_KEY))
                .setPositiveButton(bundle.getInt(OK_KEY), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("GameActivity".equals(bundle.getString(SOURCE_KEY))) {
                            GameActivity gameActivity = (GameActivity) getActivity();
                            gameActivity.onNegativeClickConfirmDialog();
                        } else {
                            DialogClickEvents activity = (DialogClickEvents) getActivity();
                            activity.positiveDialogClick(new Bundle());
                        }

                    }
                })
                .setNegativeButton(bundle.getInt(NOT_KEY), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("GameActivity".equals(bundle.getString(SOURCE_KEY))) {
                            GameActivity gameActivity = (GameActivity) getActivity();
                            gameActivity.onPositiveClickConfirmDialog();
                        } else {
                            DialogClickEvents activity = (DialogClickEvents) getActivity();
                            activity.negativeDialogClick(new Bundle());
                        }
                    }
                });
        return builder.create();
    }

    public interface DialogClickEvents {
        void positiveDialogClick(final Bundle bundle);
        void negativeDialogClick(final Bundle bundle);
    }
}
