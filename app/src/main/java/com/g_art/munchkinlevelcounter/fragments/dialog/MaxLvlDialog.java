package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.GameActivity;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;

/**
 * MunchinLevelCounter
 * Created by G_Art on 17/11/2015.
 */
public class MaxLvlDialog extends DialogFragment {

    private EditText maxLvlEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Munchkin_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.maxlvl_dialog, null);

        builder.setView(v);

        maxLvlEditText = (EditText) v.findViewById(R.id.maxLvL);
        final int currMax = getArguments().getInt(GameActivity.MAX_LVL);
        maxLvlEditText.setText(Integer.toString(currMax), TextView.BufferType.EDITABLE);

        builder.setTitle(R.string.txt_max_lvl)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_ok_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String lvl = maxLvlEditText.getText().toString();
                        if (!lvl.equals("") && !lvl.equals(" ")) {
                            try {
                                int mLvl = Integer.parseInt(lvl);
                                if (mLvl <= SettingsHandler.MIN_LVL) {
                                    Toast.makeText(getActivity(),
                                            getString(R.string.error_max_level_one),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    maxLvlEditText.setText(Integer.toString(currMax), TextView.BufferType.EDITABLE);
                                } else {
                                    ((GameActivity) getActivity()).doPositiveClickLvLDialog(mLvl);
                                }
                            } catch (NumberFormatException ex) {
                                Toast.makeText(getActivity(),
                                        getString(R.string.error_max_lvl_settings),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
                }).setNegativeButton(R.string.dialog_cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();

        final AlertDialog alertDialog = builder.create();

        maxLvlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return alertDialog;
    }
}
