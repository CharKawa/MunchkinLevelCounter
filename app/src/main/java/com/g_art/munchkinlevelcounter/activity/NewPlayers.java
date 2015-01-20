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
import com.g_art.munchkinlevelcounter.bean.Player;
import com.g_art.munchkinlevelcounter.fragments.dialog.PlayerNameDialog;
import com.g_art.munchkinlevelcounter.listadapter.CustomListAdapter;
import com.g_art.munchkinlevelcounter.util.StoredPlayers;

import java.util.ArrayList;

/**
 * Created by G_Art on 16/7/2014.
 */
public class NewPlayers extends Activity implements View.OnClickListener {

    public final static String PLAYER_KEY = "playersList";
    public final static String PLAYER_NAME = "playerName";
    private String PREFS_NO_DATA;
    final int MIN_PLAYER_QUANTITY = 2;
    private static int playerIndex = 1;
    private static boolean newPlayer = false;

    ListView listVPlayers;
    ImageButton btnAddPlayers;
    ImageButton btnClear;
    ImageButton btnStartGame;
    private DialogFragment playerDialog;
    private Bundle nBundle;
    private StoredPlayers mStored;


    private ArrayList<Player> listPlayers;
    CustomListAdapter customListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_players);

        PREFS_NO_DATA = getString(R.string.no_data);

        mStored = StoredPlayers.getInstance(PreferenceManager.getDefaultSharedPreferences(getBaseContext()));

        listVPlayers = (ListView) findViewById(R.id.listVPlayers);
        btnAddPlayers = (ImageButton) findViewById(R.id.imgBtnAddPlayer);

        playerDialog = new PlayerNameDialog();

        btnAddPlayers.setOnClickListener(this);
        btnClear = (ImageButton) findViewById(R.id.imgBtnClear);
        btnClear.setOnClickListener(this);
        btnStartGame = (ImageButton) findViewById(R.id.imgBtnStart);
        btnStartGame.setOnClickListener(this);


        if (savedInstanceState == null || !savedInstanceState.containsKey(PLAYER_KEY)) {
            if (mStored.isPlayersStored()) {
                getPlayersForNewGame();
            } else {
                listPlayers = new ArrayList<Player>();
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
        listPlayers = new ArrayList<Player>();
        if (!tList.isEmpty()) {
            for (Player player : tList) {
                listPlayers.add(player.cloneWithoutStats());
            }
        }
    }


    void showPlayerNameDialog(String name) {
        nBundle = new Bundle();
        nBundle.putString(PLAYER_NAME, name);
        playerDialog.setArguments(nBundle);
        playerDialog.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClickPlayerNameDialog(String name) {
        if (newPlayer) {
            listPlayers.add(new Player(name));
        } else {
            Player player = listPlayers.get(playerIndex);
            player.setName(name);
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
            case R.id.btnAddPlayer:
                String name = "";
                showPlayerNameDialog(name);
                newPlayer = true;
                break;
            case R.id.btnClear:
                listPlayers.clear();
                customListAdapter.notifyDataSetChanged();
                break;
            case R.id.btnStartGame:
                if (listPlayers.size() < MIN_PLAYER_QUANTITY) {
                    Toast.makeText(this, getString(R.string.more_players), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putParcelableArrayListExtra(PLAYER_KEY, listPlayers);
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
    private void playerDelete(int mPosition) {
        Player player = listPlayers.get(mPosition);

        //TODO: SHOW ALERT

        Toast.makeText(this,
                player.getName() +
                        getString(R.string.deleted),
                Toast.LENGTH_SHORT
        ).show();

        listPlayers.remove(mPosition);
        customListAdapter.notifyDataSetChanged();
    }

    private void playerEdit(int playerPosition) {
        Player player = listPlayers.get(playerPosition);
        playerIndex = playerPosition;
        newPlayer = false;
        showPlayerNameDialog(player.getName());
    }
}
