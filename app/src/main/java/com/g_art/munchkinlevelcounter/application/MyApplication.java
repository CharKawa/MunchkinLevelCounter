package com.g_art.munchkinlevelcounter.application;

import android.app.Application;
import android.util.Log;

import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.Cache;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.Products;
import org.solovyev.android.checkout.PurchaseVerifier;
import org.solovyev.android.checkout.RobotmediaDatabase;
import org.solovyev.android.checkout.RobotmediaInventory;

import java.util.concurrent.Executor;

import static java.util.Arrays.asList;
import static org.solovyev.android.checkout.ProductTypes.IN_APP;

/**
 * MunchinLevelCounter
 * Created by G_Art on 29/10/2014.
 */
public class MyApplication extends Application {

    private static final Products products = Products.create().add(IN_APP, asList("donate_099", "donate_199", "donate_399", "donate_999"));

    private final Billing billing = new Billing(this, new Billing.Configuration() {
        @Override
        public String getPublicKey() {
            return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkRwCg3RouBZQwBKzlAuvzby" +
                    "1Dwy5DgujGtsv0tBLTQB91lxSeBF7pW5c6KN2wKxwclQFJN7NgnoicY+lckrfpnsMWP" +
                    "PwYQdgMNqqyIxR8I7gbCGCb7jvVyafRWuwUQJt/+znOIyLOrbuqQTGsm0Z9Yv1tkne9" +
                    "wi0d358BT7r/2hGZ5N+jAjK5D2xXWVL1BRu7sB0mFpNVICOv/8RrqxcvaSL4FtyLh8S" +
                    "pkQHcq/zK2jQus2hGpQWaFTCSWgiv0Y9X5hFYN9P9GGuCPp4dhs6w61LlnaKVmKTuulv" +
                    "oPvtt1BzYq3lOMtPUBhYPkM35DyO/YTv0N14PXH+mTh00AUPtQIDAQAB";
        }

        @Override
        public Cache getCache() {
            return Billing.newCache();
        }

        @Override
        public PurchaseVerifier getPurchaseVerifier() {
            return null;
        }

        @Override
        public Inventory getFallbackInventory(Checkout checkout, Executor executor) {
            if (RobotmediaDatabase.exists(billing.getContext())) {
                return new RobotmediaInventory(checkout, executor);
            } else {
                return null;
            }
        }
    });

    private final Checkout checkout = Checkout.forApplication(billing, products);

    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TestMyApp", "billing.connect");
        billing.connect();
    }

    public static MyApplication get() {
        return instance;
    }

    public Checkout getCheckout() {
        return checkout;
    }
}
