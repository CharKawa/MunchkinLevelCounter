package com.g_art.munchkinlevelcounter.billing;

import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

/**
 * MunchinLevelCounter
 * Created by G_Art on 2/11/2014.
 */
public class PurchaseListener implements RequestListener<Purchase> {
    @Override
    public void onSuccess(Purchase purchase) {
        onPurchased();
    }
    
    
    public void onPurchased(){
        //TODO: create R.string.msg_thank_you_for_purchase
        Toast.makeText(getActivity(), R.string.msg_thank_you_for_purchase, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int i, Exception e) {
        // it is possible that our data is not synchronized with data on Google Play => need to handle some errors
			if (i == ResponseCodes.ITEM_ALREADY_OWNED) {
				onPurchased();
			} else {
				super.onError(i, e);
			}
    }
}
