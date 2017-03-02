package com.g_art.munchkinlevelcounter.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;
import com.g_art.munchkinlevelcounter.analytics.AnalyticsActions;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by G_Art on 12/9/2014.
 */
public class About extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		Analytics.getInstance().logEvent(AnalyticsActions.Open, "AboutActivity");

		ImageButton btnRate = (ImageButton) findViewById(R.id.imgBtnRate);
		btnRate.setOnClickListener(this);

		ImageButton imgBtnContact = (ImageButton) findViewById(R.id.imgBtnContact);
		imgBtnContact.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imgBtnRate:
				Analytics.getInstance().logEvent(AnalyticsActions.Rate, "AboutActivity");

				final Intent intent = new Intent(Intent.ACTION_VIEW);
				//Try Google Play
				intent.setData(Uri.parse("market://details?id=com.g_art.munchkinlevelcounter.paid"));
				if (!mayStartActivity(intent)) {
					//Market (Google play) app seems not installed, let's try to open a webbrowser
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.g_art.munchkinlevelcounter.paid"));
					if (!mayStartActivity(intent)) {
						//Well if this also fails, we have run out of options, inform the user.
						Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case R.id.imgBtnContact:
				Analytics.getInstance().logEvent(AnalyticsActions.Contact, "AboutActivity");

				final Intent email = new Intent(Intent.ACTION_SENDTO);
				email.setData(Uri.parse("mailto:"));
				email.putExtra(Intent.EXTRA_EMAIL, new String[]{"android.dev.g.art@gmail.com"});
				email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
				email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "");
				if (email.resolveActivity(getPackageManager()) != null) {
					startActivity(email);
				}
				break;
		}
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean mayStartActivity(Intent intent) {
		try {
			startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
}
