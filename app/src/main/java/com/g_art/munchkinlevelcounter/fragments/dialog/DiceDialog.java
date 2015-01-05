package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.fragments.game.FragmentPlayer;

/**
 * MunchinLevelCounter
 * Created by G_Art on 4/1/2015.
 */
public class DiceDialog extends DialogFragment {

    private ImageView imageView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dice_dialog, null);
        builder.setView(v);

        imageView = (ImageView) v.findViewById(R.id.imgDice);

        Bundle bundle = getArguments();
        int resId = bundle.getInt(FragmentPlayer.DICE_KEY);
        imageView.setImageResource(resId);
        builder.setTitle(R.string.dice);
        builder.setPositiveButton(R.string.dialog_ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        return builder.create();
    }
}
