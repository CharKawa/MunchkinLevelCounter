package com.g_art.munchkinlevelcounter.fragments.info;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.munchkinlevelcounter.R;

/**
 * LevelCounter
 * Created by G_Art on 1/12/2015.
 */
public class ThanksPopup extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tanks_popup, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
