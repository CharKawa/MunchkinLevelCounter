package com.g_art.munchkinlevelcounter.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.g_art.munchkinlevelcounter.bean.Player;

import java.util.ArrayList;

/**
 * Created by G_Art on 31/8/2014.
 */
public class SavePlayersStatsTask extends AsyncTask<ArrayList<Player>, Void, Boolean> {

    final String TAG = "GameActivity_Munchkin_Test";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(ArrayList<Player>... params) {

        boolean result = false;

        try {
            for (Player selectedPlayer : params[0]) {
                selectedPlayer.getLvlStats().add(String.valueOf(selectedPlayer.getLevel()));
                selectedPlayer.getGearStats().add(String.valueOf(selectedPlayer.getGear()));
                selectedPlayer.getPowerStats().add(String.valueOf(selectedPlayer.getLevel() + selectedPlayer.getGear()));
                Log.d(TAG, " Stas saved in memory");
            }
            result = true;
        } catch (Exception ex) {
            result = false;
        }

        return result;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

}
