package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.application.MyApplication;
import com.g_art.munchkinlevelcounter.model.Player;
import com.g_art.munchkinlevelcounter.model.Sex;
import com.g_art.munchkinlevelcounter.fragments.dialog.PlayerNameDialog;
import com.g_art.munchkinlevelcounter.listadapter.CustomListAdapter;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

/**
 * Created by G_Art on 16/7/2014.
 */
public class NewPlayers extends Activity implements View.OnClickListener {
    public final static String PLAYER_KEY = "playersList";
    public final static String PLAYER_NAME = "playerName";
    public static final String EMPTY_STRING = " ";
    public static final String PLAYER_SEX = "player_sex";
    private static int playerIndex = 1;
    private static boolean newPlayer = false;
    final int MIN_PLAYER_QUANTITY = 2;
    private CustomListAdapter customListAdapter;
    private Tracker mTracker;
    private String PREFS_NO_DATA;
    private DialogFragment playerDialog;
    private StoredPlayers mStored;
    private ArrayList<Player> listPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_players);

        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setAction("Setting Players")
                .setCategory("Screen")
                .setLabel("NewPlayers")
                .build());

        PREFS_NO_DATA = getString(R.string.no_data);

        mStored = StoredPlayers.getInstance(PreferenceManager.getDefaultSharedPreferences(getBaseContext()));

        ListView listVPlayers = (ListView) findViewById(R.id.listVPlayers);
        ImageButton btnAddPlayers = (ImageButton) findViewById(R.id.imgBtnAddPlayer);

        playerDialog = new PlayerNameDialog();

        btnAddPlayers.setOnClickListener(this);
        ImageButton btnClear = (ImageButton) findViewById(R.id.imgBtnClear);
        btnClear.setOnClickListener(this);
        ImageButton btnStartGame = (ImageButton) findViewById(R.id.imgBtnStart);
        btnStartGame.setOnClickListener(this);
        listPlayers = new ArrayList<Player>();


        if (savedInstanceState == null || !savedInstanceState.containsKey(PLAYER_KEY)) {
            if (mStored.isPlayersStored()) {
                getPlayersForNewGame();
            } else {
                listPlayers.add(new Player(getString(R.string.player_1)));
                listPlayers.add(new Player(getString(R.string.player_2)));
            }
        } else {
            listPlayers = savedInstanceState.getParcelableArrayList(PLAYER_KEY);
        }


        customListAdapter = new CustomListAdapter(this, listPlayers);
        listVPlayers.setAdapter(customListAdapter);
    }

    private void getPlayersForNewGame() {
        ArrayList<Player> tList = mStored.loadPlayers(PREFS_NO_DATA);
        if (!tList.isEmpty()) {
            for (Player player : tList) {
                listPlayers.add(player.cloneWithoutStats());
            }
        }
    }


    void showPlayerNameDialog(String name) {
        showPlayerNameDialog(name, Sex.MAN);
    }

    void showPlayerNameDialog(String name, Sex sex) {
        Bundle nBundle = new Bundle();
        nBundle.putString(PLAYER_NAME, name);
        nBundle.putSerializable(PLAYER_SEX, sex);
        playerDialog.setArguments(nBundle);
        playerDialog.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClickPlayerNameDialog(String name, Sex sex) {
        if (newPlayer) {
            Player newPlayer = new Player(name);
            newPlayer.setSex(sex);
            listPlayers.add(newPlayer);
        } else {
            Player player = listPlayers.get(playerIndex);
            player.setName(name);
            player.setSex(sex);
        }

        customListAdapter.notifyDataSetChanged();
    }

    public void doNegativeClickPlayerNameDialog() {
        // Do stuff here.
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PLAYER_KEY, listPlayers);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnAddPlayer:
                String name = "";
                showPlayerNameDialog(name);
                newPlayer = true;
                break;
            case R.id.imgBtnClear:
                listPlayers.clear();
                customListAdapter.notifyDataSetChanged();
                mTracker.send(new HitBuilders.EventBuilder()
                        .setAction("ClearPlayers")
                        .setCategory("Action")
                        .setLabel("MyActivity")
                        .build());
                break;
            case R.id.imgBtnStart:
                if (listPlayers.size() < MIN_PLAYER_QUANTITY) {
                    Toast.makeText(this, getString(R.string.more_players), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putParcelableArrayListExtra(PLAYER_KEY, listPlayers);

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setAction("Starting Game")
                            .setCategory("Action")
                            .setLabel("Number of players")
                            .setValue(listPlayers.size())
                            .build());

                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (playerDialog != null) {
            playerDialog = null;
        }

        super.onDestroy();
    }

    /**
     * **************  This function used by adapter ***************
     */
    public void playerDelete(int mPosition) {
        Player player = listPlayers.get(mPosition);
        StringBuilder message = new StringBuilder();
        message.append(player.getName())
                .append(EMPTY_STRING)
                .append(getString(R.string.deleted));

        Toast.makeText(this, message.toString(),
                Toast.LENGTH_SHORT
        ).show();

        listPlayers.remove(mPosition);
        customListAdapter.notifyDataSetChanged();
    }

    public void playerEdit(int playerPosition) {
        Player player = listPlayers.get(playerPosition);
        playerIndex = playerPosition;
        newPlayer = false;
        showPlayerNameDialog(player.getName(), player.getSex());
    }

    public void playerChangeSex(int playerPosition) {
        Player player = listPlayers.get(playerPosition);
        if (Sex.MAN == player.getSex()) {
            player.setSex(Sex.WOMAN);
        } else {
            player.setSex(Sex.MAN);
        }
        customListAdapter.notifyDataSetChanged();
    }
}
