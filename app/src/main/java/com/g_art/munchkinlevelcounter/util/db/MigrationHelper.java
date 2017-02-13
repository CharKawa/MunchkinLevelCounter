package com.g_art.munchkinlevelcounter.util.db;

import android.content.Context;
import android.preference.PreferenceManager;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;

import java.util.List;

/**
 * Created by agulia on 2/1/17.
 */

public class MigrationHelper {

	public static void migrateFromSharedPrefsToDatabase(Context baseContext) {
		StoredPlayers mStored = StoredPlayers.getInstance(PreferenceManager.getDefaultSharedPreferences(baseContext));
		if (mStored.isPlayersStored()) {
			final List<Player> playerList = mStored.loadPlayers(baseContext.getString(R.string.no_data));
			if (playerList == null || playerList.isEmpty()) {
				return;
			}

			final EntityManager em = EntityManager.getInstance();

			long id = 0l;
			for (Player player : playerList) {
				player.setId(++id);
				em.insertPlayer(player);
			}
			mStored.clearStoredPlayers();
		}
	}
}
