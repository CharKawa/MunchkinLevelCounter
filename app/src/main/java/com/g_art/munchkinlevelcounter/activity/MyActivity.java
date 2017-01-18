package com.g_art.munchkinlevelcounter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MyActivity extends AppCompatActivity {
	private Tracker mTracker;
	private Unbinder unbinder;
	private SettingsHandler settingsHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_main_screen);
		// Obtain the shared Tracker instance.
		MyApplication application = (MyApplication) getApplication();
		mTracker = application.getDefaultTracker();
		mTracker.send(new HitBuilders.EventBuilder()
				.setAction("AppStarted")
				.setCategory("Screen")
				.setLabel("MyActivity")
				.build());

		unbinder = ButterKnife.bind(this);

		final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		settingsHandler = SettingsHandler.getInstance(mPrefs);
		int popupStatus = settingsHandler.getPopupStatus();

		showTranslationPopup(popupStatus);
	}

	private void showTranslationPopup(int popupStatus) {
		if (popupStatus == SettingsHandler.NEVER_ASK) {
			return;
		}
		new MaterialDialog.Builder(this)
				.title(R.string.tr_dialog_title)
				.titleColor(getResources().getColor(R.color.text_color))
				.content(R.string.tr_dialog_msg)
				.contentColor(getResources().getColor(R.color.text_color))
				.positiveText(R.string.tr_dialog_pos)
				.positiveColor(getResources().getColor(R.color.text_color))
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						mTracker.send(new HitBuilders.EventBuilder()
								.setAction("btn_translation_help")
								.setCategory("Button")
								.setLabel("Translation.Help")
								.build());

						updateStatus(SettingsHandler.NEVER_ASK);

						final Intent email = new Intent(Intent.ACTION_SENDTO);
						email.setData(Uri.parse("mailto:"));
						email.putExtra(Intent.EXTRA_EMAIL, new String[]{"android.dev.g.art@gmail.com"});
						email.putExtra(Intent.EXTRA_SUBJECT, "Translation");
						email.putExtra(Intent.EXTRA_TEXT, "Yes, I want to help you with translation into: ");
						if (email.resolveActivity(getPackageManager()) != null) {
							startActivity(email);
						}
					}
				})
				.neutralText(R.string.tr_dialog_neu)
				.neutralColor(getResources().getColor(R.color.text_color))
				.onNeutral(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						mTracker.send(new HitBuilders.EventBuilder()
								.setAction("btn_translation_not_now")
								.setCategory("Button")
								.setLabel("Translation.Help")
								.build());
						updateStatus(SettingsHandler.ASK_LATER);
						dialog.dismiss();
					}
				})
				.negativeText(R.string.tr_dialog_neg)
				.negativeColor(getResources().getColor(R.color.text_color))
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						mTracker.send(new HitBuilders.EventBuilder()
								.setAction("btn_translation_no")
								.setCategory("Button")
								.setLabel("Translation.Help")
								.build());
						updateStatus(SettingsHandler.NEVER_ASK);
					}
				})
				.backgroundColor(getResources().getColor(R.color.background))
				.show();
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

	@OnClick(R.id.btnPlayers)
	public void start() {
		Intent intent = new Intent(this, NewPlayers.class);
		startActivity(intent);
	}

	private void updateStatus(int updateStatus) {
		if (settingsHandler == null) {
			final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			settingsHandler = SettingsHandler.getInstance(mPrefs);
		}
		settingsHandler.updateStatus(updateStatus);
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
