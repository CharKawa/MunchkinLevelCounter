package com.g_art.munchkinlevelcounter.listadapter.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by agulia on 11/28/16.
 */

/**
 * Listener for manual initiation of a drag.
 */
public interface OnStartDragListener {

	/**
	 * Called when a view is requesting a start of a drag.
	 *
	 * @param viewHolder The holder of the view to drag.
	 */
	void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
