package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.fragments.info.ThanksPopup;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;


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
        showDialog();
    }

    public void showDialog() {
        //todo check for first open

        FragmentManager fragmentManager = getFragmentManager();
        ThanksPopup newFragment = new ThanksPopup();

        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_stats:
                StoredPlayers mStored = StoredPlayers.getInstance(PreferenceManager.getDefaultSharedPreferences(getBaseContext()));
                if (mStored.isPlayersStored()) {
                    intent = new Intent(this, Stats.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.action_about:
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.frag_main_screen, container, false);

            ImageButton btnPlayers = (ImageButton) rootView.findViewById(R.id.btnPlayers);
            btnPlayers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewPlayers.class);
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }
}
