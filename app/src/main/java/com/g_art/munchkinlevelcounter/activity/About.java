package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.billing.IabHelper;
import com.g_art.munchkinlevelcounter.billing.IabResult;
import com.g_art.munchkinlevelcounter.billing.util.Inventory;
import com.g_art.munchkinlevelcounter.billing.util.Purchase;

/**
 * Created by G_Art on 12/9/2014.
 */
public class About extends Activity implements View.OnClickListener {

    // Debug tag, for logging
    static final String TAG = "AboutActivity";

    private Button btnRate;
    private Button btnDonate099;
    private Button btnDonate199;
    private Button btnDonate399;
    private Button btnDonate999;

    private boolean isDonate = false;

    // The helper object
    IabHelper mHelper;

    // SKUs for our products
    static final String SKU_DONATE_099 = "donate_099";
    static final String SKU_DONATE_199 = "donate_199";
    static final String SKU_DONATE_399 = "donate_399";
    static final String SKU_DONATE_999 = "donate_999";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        String base64EncodedPublicKey = getString(R.string.base64);

        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

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

        btnRate = (Button) findViewById(R.id.btn_Rate);
        btnRate.setOnClickListener(this);

        if (isDonate) {
            btnDonate099 = (Button) findViewById(R.id.btn_donate_099);
            btnDonate099.setOnClickListener(this);

            btnDonate199 = (Button) findViewById(R.id.btn_donate_199);
            btnDonate199.setOnClickListener(this);

            btnDonate399 = (Button) findViewById(R.id.btn_donate_399);
            btnDonate399.setOnClickListener(this);

            btnDonate999 = (Button) findViewById(R.id.btn_donate_999);
            btnDonate999.setOnClickListener(this);
        }
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
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

            Purchase purchase099 = inv.getPurchase(SKU_DONATE_099);
            if (purchase099 != null && verifyDeveloperPayload(purchase099)) {
                Log.d(TAG, "We have purchase099. Consuming it.");
                mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_099), mConsumeFinishedListener);
                return;
            }

            Purchase purchase199 = inv.getPurchase(SKU_DONATE_199);
            if (purchase199 != null && verifyDeveloperPayload(purchase199)) {
                Log.d(TAG, "We have purchase099. Consuming it.");
                mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_199), mConsumeFinishedListener);
                return;
            }

            Purchase purchase399 = inv.getPurchase(SKU_DONATE_399);
            if (purchase399 != null && verifyDeveloperPayload(purchase399)) {
                Log.d(TAG, "We have purchase099. Consuming it.");
                mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_399), mConsumeFinishedListener);
                return;
            }

            Purchase purchase999 = inv.getPurchase(SKU_DONATE_999);
            if (purchase999 != null && verifyDeveloperPayload(purchase999)) {
                Log.d(TAG, "We have purchase099. Consuming it.");
                mHelper.consumeAsync(inv.getPurchase(SKU_DONATE_999), mConsumeFinishedListener);
                return;
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Rate:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Try Google Play
                intent.setData(Uri.parse("market://details?id=com.g_art.munchkinlevelcounter"));
                if (!MyStartActivity(intent)) {
                    //Market (Google play) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.g_art.munchkinlevelcounter"));
                    if (!MyStartActivity(intent)) {
                        //Well if this also fails, we have run out of options, inform the user.
                        Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_donate_099:
                if (isDonate) {
                    onDonateBtnClicked(SKU_DONATE_099);
                }
                break;
            case R.id.btn_donate_199:
                if (isDonate) {
                    onDonateBtnClicked(SKU_DONATE_199);
                }
                break;
            case R.id.btn_donate_399:
                if (isDonate) {
                    onDonateBtnClicked(SKU_DONATE_399);
                }
                break;
            case R.id.btn_donate_999:
                if (isDonate) {
                    onDonateBtnClicked(SKU_DONATE_999);
                }
                break;
        }
    }

    private boolean MyStartActivity(Intent intent) {
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

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
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

            if (info.getSku().equals(SKU_DONATE_099)) {
                // bought SKU_DONATE_099. So consume it.
                Log.d(TAG, "Purchase is SKU_DONATE_099. Starting SKU_DONATE_099 consumption.");
                mHelper.consumeAsync(info, mConsumeFinishedListener);
            } else if (info.getSku().equals(SKU_DONATE_199)) {
                // bought SKU_DONATE_199. So consume it.
                Log.d(TAG, "Purchase is SKU_DONATE_199. Starting SKU_DONATE_199 consumption.");
                mHelper.consumeAsync(info, mConsumeFinishedListener);
            } else if (info.getSku().equals(SKU_DONATE_399)) {
                // bought SKU_DONATE_399. So consume it.
                Log.d(TAG, "Purchase is SKU_DONATE_399. Starting SKU_DONATE_399 consumption.");
                mHelper.consumeAsync(info, mConsumeFinishedListener);
            } else if (info.getSku().equals(SKU_DONATE_999)) {
                // bought SKU_DONATE_999. So consume it.
                Log.d(TAG, "Purchase is SKU_DONATE_999. Starting SKU_DONATE_999 consumption.");
                mHelper.consumeAsync(info, mConsumeFinishedListener);
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
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

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
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
}
