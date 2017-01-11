package com.g_art.munchkinlevelcounter.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MyActivity extends AppCompatActivity {
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setAction("AppStarted")
                .setCategory("Screen")
                .setLabel("MyActivity")
                .build());

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        doNeedToOpenThanks();
    }

    private void doNeedToOpenThanks() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SettingsHandler settingsHandler = SettingsHandler.getInstance(mPrefs);
        int popupStatus = settingsHandler.getPopupStatus();
        if (popupStatus == SettingsHandler.FIRST_SHOW) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setAction("OpenThanksScreen")
                    .setCategory("Screen")
                    .setLabel("FIRST_SHOW")
                    .build());
            showDialog();
        } else if (popupStatus == SettingsHandler.ASK_LATER) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setAction("OpenThanksScreen")
                    .setCategory("Screen")
                    .setLabel("ASK_LATER")
                    .build());
            Date dateOfShow = settingsHandler.getStatusDate();
            long DAY_IN_MS = 1000 * 60 * 60 * 24;
            Date currentDate = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
            if (dateOfShow.before(currentDate)) {
                showDialog();
            }
        }
    }

    private void showDialog() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
