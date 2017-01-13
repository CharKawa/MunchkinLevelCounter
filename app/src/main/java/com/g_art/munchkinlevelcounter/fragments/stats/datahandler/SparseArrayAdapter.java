package com.g_art.munchkinlevelcounter.fragments.stats.datahandler;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by G_Art on 7/9/2014.
 */
class SparseArrayAdapter<E> extends BaseAdapter {


	private SparseArray<E> mData;

	public void setData(SparseArray<E> data) {
		mData = data;
	}

	@Override
	public int getCount() {
		if (mData != null && mData.size() >= 1) {
			return mData.size();
		} else {
			return 1;
		}

	}

	@Override
	public E getItem(int position) {
		return mData.valueAt(position);
	}

	@Override
	public long getItemId(int position) {
		return mData.keyAt(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}
