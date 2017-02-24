package com.g_art.munchkinlevelcounter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;
import com.g_art.munchkinlevelcounter.analytics.AnalyticsActions;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class StartActivity extends AppCompatActivity {
	private Unbinder unbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		Analytics.getInstance().logEvent(AnalyticsActions.Open, "StartActivity");

		unbinder = ButterKnife.bind(this);
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
				final StoredPlayers mStored = StoredPlayers.getInstance(PreferenceManager.getDefaultSharedPreferences(getBaseContext()));
				if (mStored.isPlayersStored()) {
					intent = new Intent(this, Stats.class);
					startActivity(intent);
				} else {
					Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
				}
				return true;
			case R.id.action_settings:
				intent = new Intent(this, PreferenceScreen.class);
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

	@OnClick({
			R.id.btn_game_master_mode,
			R.id.imb_game_master_mode
	})
	public void startGameMasterMode() {
		Intent intent = new Intent(this, NewPlayers.class);
		startActivity(intent);
	}

	@OnClick({
			R.id.btn_single_mode,
			R.id.imb_single_mode
	})
	public void startSingleMode() {
		Intent intent = new Intent(this, SingleModePrepareActivity.class);
		startActivity(intent);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	protected void onDestroy() {
		unbinder.unbind();
		super.onDestroy();
	}
}
