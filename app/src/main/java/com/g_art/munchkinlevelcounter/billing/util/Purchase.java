package com.g_art.munchkinlevelcounter.billing.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * MunchinLevelCounter
 * Created by G_Art on 7/12/2014.
 * <p/>
 * Represents an in-app billing purchase.
 */
public class Purchase {
	private final String mItemType;  // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS
	private final String mOriginalJson;
	private String mOrderId;
	private String mPackageName;
	private String mSku;
	private long mPurchaseTime;
	private int mPurchaseState;
	private String mDeveloperPayload;
	private String mToken;
	private String mSignature;

	public Purchase(String itemType, String jsonPurchaseInfo, String signature) throws JSONException {
		mItemType = itemType;
		mOriginalJson = jsonPurchaseInfo;
		JSONObject o = new JSONObject(mOriginalJson);
		mOrderId = o.optString("orderId");
		mPackageName = o.optString("packageName");
		mSku = o.optString("productId");
		mPurchaseTime = o.optLong("purchaseTime");
		mPurchaseState = o.optInt("purchaseState");
		mDeveloperPayload = o.optString("developerPayload");
		mToken = o.optString("token", o.optString("purchaseToken"));
		mSignature = signature;
	}

	public String getItemType() {
		return mItemType;
	}

	public String getOrderId() {
		return mOrderId;
	}

	public String getPackageName() {
		return mPackageName;
	}

	public String getSku() {
		return mSku;
	}

	public long getPurchaseTime() {
		return mPurchaseTime;
	}

	public int getPurchaseState() {
		return mPurchaseState;
	}

	public String getDeveloperPayload() {
		return mDeveloperPayload;
	}

	public String getToken() {
		return mToken;
	}

	public String getOriginalJson() {
		return mOriginalJson;
	}

	public String getSignature() {
		return mSignature;
	}

	@Override
	public String toString() {
		return "PurchaseInfo(type:" + mItemType + "):" + mOriginalJson;
	}
}

