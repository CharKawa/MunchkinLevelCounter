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

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by G_Art on 12/9/2014.
 */
public class About extends AppCompatActivity implements View.OnClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// Obtain the shared Tracker instance.
		MyApplication application = (MyApplication) getApplication();
		Answers.getInstance().logCustom(new CustomEvent("About opened"));

		ImageButton btnRate = (ImageButton) findViewById(R.id.imgBtnRate);
		btnRate.setOnClickListener(this);

		ImageButton imgBtnContact = (ImageButton) findViewById(R.id.imgBtnContact);
		imgBtnContact.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imgBtnRate:
				Answers.getInstance().logCustom(new CustomEvent("Rate clicked"));
				final Intent intent = new Intent(Intent.ACTION_VIEW);
				//Try Google Play
				intent.setData(Uri.parse("http://www.amazon.com/gp/mas/dl/android?asin=B0196EYN12"));
				if (!mayStartActivity(intent)) {
					//Market (Google play) app seems not installed, let's try to open a webbrowser
					Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.imgBtnContact:
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
