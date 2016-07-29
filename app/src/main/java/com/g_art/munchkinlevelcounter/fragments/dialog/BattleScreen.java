package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import com.g_art.munchkinlevelcounter.R;

/**
 * MunchkinLevelCounter
 * Created by fftem on 27-Jul-16.
 */

public class BattleScreen extends DialogFragment {

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		View v = inflater.inflate(R.layout.fragment_battle, null);

		builder.setView(v);
		return builder.create();
	}
}
