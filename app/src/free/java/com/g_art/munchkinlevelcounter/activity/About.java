package com.g_art.munchkinlevelcounter.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;
import com.g_art.munchkinlevelcounter.analytics.AnalyticsActions;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.billing.IabHelper;
import com.g_art.munchkinlevelcounter.billing.IabResult;
import com.g_art.munchkinlevelcounter.billing.util.Inventory;
import com.g_art.munchkinlevelcounter.billing.util.Purchase;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by G_Art on 12/9/2014.
 */
public class About extends AppCompatActivity implements View.OnClickListener {

	// Debug tag, for logging
	private static final String TAG = "AboutActivity";
	// SKUs for our products
	private static final String SKU_DONATE_099 = "donate_099";
	private static final String SKU_DONATE_199 = "donate_199";
	private static final String SKU_DONATE_399 = "donate_399";
	private static final String SKU_DONATE_999 = "donate_999";
	// (arbitrary) request code for the purchase flow
	private static final int RC_REQUEST = 10001;
	// The helper object
	private IabHelper mHelper;
	// Called when consumption is complete
	private final IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null) return;

			if (result.isSuccess()) {
				Log.d(TAG, "Consumption successful. Provisioning.");
				alert("Thanks for your donating!!");
			} else {
				complain("Error while consuming: " + result);
			}
			Log.d(TAG, "End consumption flow.");
		}
	};
	// Listener that's called when we finish querying the items and subscriptions we own
	private final IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {
			Log.d(TAG, "Query inventory finished.");

			//Have we been disposed of in the meantime? if so, quit.
			if (mHelper == null) return;

			//Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

            /*
			 * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

			final Purchase purchase099 = inv.getPurchase(SKU_DONATE_099);
			if (purchase099 != null && verifyDeveloperPayload(purchase099)) {
				Log.d(TAG, "We have purchase099. Consuming it.");
				mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_099), mConsumeFinishedListener);
				return;
			}

			final Purchase purchase199 = inv.getPurchase(SKU_DONATE_199);
			if (purchase199 != null && verifyDeveloperPayload(purchase199)) {
				Log.d(TAG, "We have purchase099. Consuming it.");
				mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_199), mConsumeFinishedListener);
				return;
			}

			final Purchase purchase399 = inv.getPurchase(SKU_DONATE_399);
			if (purchase399 != null && verifyDeveloperPayload(purchase399)) {
				Log.d(TAG, "We have purchase099. Consuming it.");
				mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_399), mConsumeFinishedListener);
				return;
			}

			final Purchase purchase999 = inv.getPurchase(SKU_DONATE_999);
			if (purchase999 != null && verifyDeveloperPayload(purchase999)) {
				Log.d(TAG, "We have purchase099. Consuming it.");
				mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_999), mConsumeFinishedListener);
				return;
			}

			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	// Callback for when a purchase is finished
	private final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase info) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + info);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null) return;

			if (result.isFailure()) {
				complain("Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(info)) {
				complain("Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			switch (info.getSku()) {
				case SKU_DONATE_099:
					// bought SKU_DONATE_099. So consume it.
					Log.d(TAG, "Purchase is SKU_DONATE_099. Starting SKU_DONATE_099 consumption.");
					mHelper.consumeAsync(info, mConsumeFinishedListener);
					break;
				case SKU_DONATE_199:
					// bought SKU_DONATE_199. So consume it.
					Log.d(TAG, "Purchase is SKU_DONATE_199. Starting SKU_DONATE_199 consumption.");
					mHelper.consumeAsync(info, mConsumeFinishedListener);
					break;
				case SKU_DONATE_399:
					// bought SKU_DONATE_399. So consume it.
					Log.d(TAG, "Purchase is SKU_DONATE_399. Starting SKU_DONATE_399 consumption.");
					mHelper.consumeAsync(info, mConsumeFinishedListener);
					break;
				case SKU_DONATE_999:
					// bought SKU_DONATE_999. So consume it.
					Log.d(TAG, "Purchase is SKU_DONATE_999. Starting SKU_DONATE_999 consumption.");
					mHelper.consumeAsync(info, mConsumeFinishedListener);
					break;
			}
		}
	};

	private boolean isDonate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// Obtain the shared Tracker instance.
		MyApplication application = (MyApplication) getApplication();

		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this);

		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		try {
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				public void onIabSetupFinished(IabResult result) {
					Log.d(TAG, "Setup finished.");

					if (!result.isSuccess()) {
						// Oh noes, there was a problem.
						complain("Problem setting up in-app billing: " + result);
						return;
					}

					// Have we been disposed of in the meantime? If so, quit.
					if (mHelper == null) return;

					// IAB is fully set up. Now, let's get an inventory of stuff we own.
					Log.d(TAG, "Setup successful. Querying inventory.");
					mHelper.queryInventoryAsync(mGotInventoryListener);
					isDonate = true;
				}
			});
		} catch (Exception e) {
			Log.d(TAG, "Exception in Billing");
			isDonate = false;
		}

		ImageButton btnRate = (ImageButton) findViewById(R.id.imgBtnRate);
		btnRate.setOnClickListener(this);

		ImageButton imgBtnContact = (ImageButton) findViewById(R.id.imgBtnContact);
		imgBtnContact.setOnClickListener(this);

		ImageButton btnDonate099 = (ImageButton) findViewById(R.id.btn_donate_099);
		btnDonate099.setOnClickListener(this);

		ImageButton btnDonate199 = (ImageButton) findViewById(R.id.btn_donate_199);
		btnDonate199.setOnClickListener(this);

		ImageButton btnDonate399 = (ImageButton) findViewById(R.id.btn_donate_399);
		btnDonate399.setOnClickListener(this);

		ImageButton btnDonate999 = (ImageButton) findViewById(R.id.btn_donate_999);
		btnDonate999.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imgBtnRate:
				Analytics.getInstance().logEvent(AnalyticsActions.Rate, "AboutActivity");

				final Intent intent = new Intent(Intent.ACTION_VIEW);
				//Try Google Play
				intent.setData(Uri.parse("market://details?id=com.g_art.munchkinlevelcounter"));
				if (!mayStartActivity(intent)) {
					//Market (Google play) app seems not installed, let's try to open a webbrowser
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.g_art.munchkinlevelcounter"));
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
			case R.id.btn_donate_099:
				Analytics.getInstance().logEvent(AnalyticsActions.Donate, "BtnDonate_099", "AboutActivity");

				if (isDonate) {
					onDonateBtnClicked(SKU_DONATE_099);
				} else {
					Toast.makeText(this, "Could not connect to Android market, please install the market app.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_donate_199:
				Analytics.getInstance().logEvent(AnalyticsActions.Donate, "BtnDonate_199", "AboutActivity");

				if (isDonate) {
					onDonateBtnClicked(SKU_DONATE_199);
				} else {
					Toast.makeText(this, "Could not connect to Android market, please install the market app.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_donate_399:
				Analytics.getInstance().logEvent(AnalyticsActions.Donate, "BtnDonate_399", "AboutActivity");

				if (isDonate) {
					onDonateBtnClicked(SKU_DONATE_399);
				} else {
					Toast.makeText(this, "Could not connect to Android market, please install the market app.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_donate_999:
				Analytics.getInstance().logEvent(AnalyticsActions.Donate, "BtnDonate_999", "AboutActivity");

				if (isDonate) {
					onDonateBtnClicked(SKU_DONATE_999);
				} else {
					Toast.makeText(this, "Could not connect to Android market, please install the market app.", Toast.LENGTH_SHORT).show();
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

	private void onDonateBtnClicked(String SKU) {
		// launch the sku purchase
		// We will be notified of completion via mPurchaseFinishedListener

		Log.d(TAG, "Launching purchase flow for: " + SKU);

         /* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
		String payload = "";

		mHelper.launchPurchaseFlow(this, SKU, RC_REQUEST,
				mPurchaseFinishedListener, payload);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if (mHelper == null) return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	private void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
		Analytics.getInstance().logEvent(AnalyticsActions.Donate, "Error", "AboutActivity");
	}

	private void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	/**
	 * Verifies the developer payload of a purchase.
	 */
	private boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

        /*
		 * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			if (isDonate) {
				mHelper.dispose();
			}
			mHelper = null;
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
}
