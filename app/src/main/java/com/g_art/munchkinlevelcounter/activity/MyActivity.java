package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.g_art.munchkinlevelcounter.R;


public class MyActivity extends Activity {

    final String TAG = "Munchkin";

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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "MainActivity: onDestroy()");
        super.onDestroy();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        final String LOG_TAG = "mainFrag";

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
                            Log.d(LOG_TAG, "Button Players clicked");
                            intent = new Intent(getActivity(), NewPlayers.class);
                            startActivity(intent);
                            break;
                        case R.id.btnStats:
                            intent = new Intent(getActivity(), Stats.class);
                            startActivity(intent);
                            Log.d(LOG_TAG, "Button Stats clicked");
                            break;
                        case R.id.btnSett:
                            intent = new Intent(getActivity(), Settings.class);
                            startActivity(intent);
                            Log.d(LOG_TAG, "Button Settings clicked");
                            break;
                        case R.id.btnAbout:
                            Log.d(LOG_TAG, "Button About clicked");
                            break;
                    }
                }
            };

            Button btnPlayers = (Button) rootView.findViewById(R.id.btnPlayers);
            btnPlayers.setOnClickListener(clickListener);

            Button btnStats = (Button) rootView.findViewById(R.id.btnStats);
            btnStats.setOnClickListener(clickListener);

            Button btnSett = (Button) rootView.findViewById(R.id.btnSett);
            btnSett.setOnClickListener(clickListener);

            Button btnAbout = (Button) rootView.findViewById(R.id.btnAbout);
            btnAbout.setOnClickListener(clickListener);

            return rootView;
        }
    }
}
