package com.g_art.munchkinlevelcounter.fragments.dialog;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.g_art.munchkinlevelcounter.R;

/**
 * MunchkinLevelCounter
 * Created by fftem on 27-Jul-16.
 */

public class BattleScreen extends Fragment {
	private View view;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_battle, container, true);
		return view;
	}
	//	@NonNull
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//		LayoutInflater inflater = getActivity().getLayoutInflater();
//
//		View v = inflater.inflate(R.layout.fragment_battle, null);
//
//		builder.setView(v);
//		Dialog dialog = builder.create();
//		return dialog;
//	}

//	@Override
//	public void onResume() {
//		// Get existing layout params for the window
//		ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//		// Assign window properties to fill the parent
//		params.width = WindowManager.LayoutParams.MATCH_PARENT;
//		params.height = WindowManager.LayoutParams.MATCH_PARENT;
//		getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//		// Call super onResume after sizing
//		super.onResume();
//	}
}
