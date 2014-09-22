package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.g_art.munchkinlevelcounter.R;


public class MyActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.frag_main_screen, container, false);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    switch (v.getId()) {
                        case R.id.btnPlayers:
                            intent = new Intent(getActivity(), NewPlayers.class);
                            startActivity(intent);
                            break;
                        case R.id.btnStats:
                            intent = new Intent(getActivity(), Stats.class);
                            startActivity(intent);
                            break;
                        case R.id.btnSett:
                            intent = new Intent(getActivity(), Settings.class);
                            startActivity(intent);
                            break;
                        case R.id.btnAbout:
                            intent = new Intent(getActivity(), About.class);
                            startActivity(intent);
                            break;
                    }
                }
            };

            ImageButton btnPlayers = (ImageButton) rootView.findViewById(R.id.btnPlayers);
            btnPlayers.setOnClickListener(clickListener);

            ImageButton btnStats = (ImageButton) rootView.findViewById(R.id.btnStats);
            btnStats.setOnClickListener(clickListener);

            ImageButton btnSett = (ImageButton) rootView.findViewById(R.id.btnSett);
            btnSett.setOnClickListener(clickListener);

            ImageButton btnAbout = (ImageButton) rootView.findViewById(R.id.btnAbout);
            btnAbout.setOnClickListener(clickListener);

            return rootView;
        }
    }
}
