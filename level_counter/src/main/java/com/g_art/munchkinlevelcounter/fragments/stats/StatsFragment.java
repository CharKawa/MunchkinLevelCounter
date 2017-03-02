package com.g_art.munchkinlevelcounter.fragments.stats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.Stats;
import com.g_art.munchkinlevelcounter.fragments.stats.datahandler.StatsHandler;
import com.g_art.munchkinlevelcounter.model.Player;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;

import java.util.ArrayList;

/**
 * Created by agulia on 1/19/17.
 */

public abstract class StatsFragment extends Fragment {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.stats, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.stats_save:
				saveStatsToFile();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	protected void initView(View v, int statsType) {
		boolean isDataPresent = false;
		ArrayList<Player> playersList = null;

		if (getArguments() != null) {
			playersList = getArguments().getParcelableArrayList(Stats.PLAYER_KEY);
			isDataPresent = playersList != null && !playersList.isEmpty();
		}

		if (isDataPresent) {
			final LineChart mChart = (LineChart) v.findViewById(R.id.game_stats);
			mChart.setDrawGridBackground(false);
			mChart.setBackgroundColor(getResources().getColor(R.color.background));
			// enable touch gestures
			mChart.setTouchEnabled(true);
			mChart.setScaleEnabled(true);
			// if disabled, scaling can be done on x- and y-axis separately
			mChart.setPinchZoom(true);

			// no description text
			mChart.getDescription().setEnabled(false);
			mChart.getLegend().setWordWrapEnabled(true);
			mChart.getLegend().setTextColor(getResources().getColor(R.color.text_color));
			mChart.getLegend().setTextSize(15f);
			mChart.getLegend().setXEntrySpace(15f);
			mChart.setBorderColor(getResources().getColor(R.color.text_color));

			Description desc = new Description();
			desc.setText(getResources().getString(R.string.lvl_tab));
			desc.setTextColor(getResources().getColor(R.color.text_color));
			desc.setTextSize(15f);
			switch (statsType) {
				case StatsHandler.GEAR_STATS:
					desc.setText(getResources().getString(R.string.gear_tab));
					break;
				case StatsHandler.POWER_STATS:
					desc.setText(getResources().getString(R.string.power_tab));
					break;
				default:
					break;
			}

			mChart.setDescription(desc);

			final SparseArray<String> playersColors = StatsHandler.getStats(playersList, mChart, getActivity(), statsType);
			if (playersColors != null) {
				mChart.invalidate();
			}
		}
	}

	private void saveStatsToFile() {
		final View v = getView();
		if (v != null) {
			final LineChart mChart = (LineChart) v.findViewById(R.id.game_stats);
			final String fileName = String.format(getResources().getString(R.string.save_stats_file_name), SystemClock.currentThreadTimeMillis());
			final boolean result = mChart.saveToGallery(fileName, getResources().getString(R.string.app_name),
					"MPAndroidChart-Library Save", Bitmap.CompressFormat.JPEG, 100);

			if (result) {
				new MaterialDialog.Builder(getActivity())
						.content(R.string.save_stats_result)
						.positiveText(R.string.open_gallery)
						.onPositive(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setType("image/*");
								startActivity(Intent.createChooser(intent, "Choose Gallery"));
							}
						})
						.neutralText(R.string.dialog_cancel_btn)
						.onNeutral(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								dialog.dismiss();
							}
						})
						.show();
			} else {
				Toast.makeText(getContext(), R.string.save_stats_fail, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
