package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;
import org.solovyev.android.checkout.ResponseCodes;
import org.solovyev.android.checkout.Sku;

import java.util.ArrayList;
import java.util.List;

import static org.solovyev.android.checkout.ProductTypes.IN_APP;

/**
 * Created by G_Art on 12/9/2014.
 */
public class About extends Activity implements View.OnClickListener {

    private Inventory inventory;
    private final ActivityCheckout checkout = Checkout.forActivity(this, MyApplication.get().getCheckout());
    private List<Sku> skuList;

    private Button btnRate;
    private Button btnDonate099;
    private Button btnDonate199;
    private Button btnDonate399;
    private Button btnDonate999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        checkout.start();
        checkout.createPurchaseFlow(new PurchaseListener());

        inventory = checkout.loadInventory();
        inventory.whenLoaded(new InventoryLoadedListener());

        btnRate = (Button) findViewById(R.id.btn_Rate);
        btnRate.setOnClickListener(this);

        btnDonate099 = (Button) findViewById(R.id.btn_donate_099);
        btnDonate099.setOnClickListener(this);

        btnDonate199 = (Button) findViewById(R.id.btn_donate_199);
        btnDonate199.setOnClickListener(this);

        btnDonate399 = (Button) findViewById(R.id.btn_donate_399);
        btnDonate399.setOnClickListener(this);

        btnDonate999 = (Button) findViewById(R.id.btn_donate_999);
        btnDonate999.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Rate:
                break;
            case R.id.btn_donate_099:
                purchase(inventory.getProducts().get(IN_APP).getSkus().get(0));
                break;
            case R.id.btn_donate_199:
                break;
            case R.id.btn_donate_399:
                break;
            case R.id.btn_donate_999:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkout.onActivityResult(requestCode, resultCode, data);
    }

    private void purchase(final Sku sku) {
        checkout.whenReady(new Checkout.ListenerAdapter() {
            @Override
            public void onReady(BillingRequests requests) {
                Log.d("Tag", "onPurchased ");
                requests.purchase(sku, null, checkout.getPurchaseFlow());
            }
        });
    }

    @Override
    protected void onDestroy() {
        checkout.destroyPurchaseFlow();
        checkout.stop();
        super.onDestroy();
    }

    private class PurchaseListener implements RequestListener<Purchase> {
        @Override
        public void onSuccess(Purchase purchase) {
            onPurchased();
        }


        public void onPurchased() {
            Log.d("Tag", "onPurchased ");
            inventory.load().whenLoaded(new InventoryLoadedListener());
            Toast.makeText(getApplicationContext(), R.string.msg_thank_you_for_purchase, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(int i, Exception e) {
            // it is possible that our data is not synchronized with data on Google Play => need to handle some errors
            if (i == ResponseCodes.ITEM_ALREADY_OWNED) {
                onPurchased();
            } else {
                Toast.makeText(getApplicationContext(), R.string.msg_error_during_purchase, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class InventoryLoadedListener implements Inventory.Listener {
        @Override
        public void onLoaded(Inventory.Products products) {
            skuList = new ArrayList<Sku>();
            Log.d("Tag", "products " + products.get(IN_APP));
            final Inventory.Product product = products.get(IN_APP);
            Log.d("Tag", "Skus " + product.getSkus());
            if (product.supported) {
                for (Sku sku : product.getSkus()) {
                    final Purchase purchase = product.getPurchaseInState(sku, Purchase.State.PURCHASED);
                    Log.d("Tag", "purchase! " + purchase);
                    skuList.add(sku);
                    Log.d("Tag", "Sku! " + sku.title);

                }
            } else {
                Log.d("Tag", "product.supported: " + product.supported);
            }

        }
    }
}
