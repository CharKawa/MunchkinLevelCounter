package com.g_art.munchkinlevelcounter.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
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
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G_Art on 16/7/2014.
 */
public class NewPlayers extends Activity implements View.OnClickListener {
    private final static String PLAYER_KEY = "playersList";
    public final static String PLAYER_NAME = "playerName";
    private static final String EMPTY_STRING = " ";
    public static final String PLAYER_SEX = "player_sex";
    private static int playerIndex = 1;
    private static boolean newPlayer = false;
	private CustomListAdapter customListAdapter;
    private Tracker mTracker;
    private String PREFS_NO_DATA;
    private DialogFragment playerDialog;
    private StoredPlayers mStored;
	private ArrayList<Pair<Long, Player>> mPlayersList;

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

		DragListView mDragList = (DragListView) findViewById(R.id.listVPlayers);
        ImageButton btnAddPlayers = (ImageButton) findViewById(R.id.imgBtnAddPlayer);


        btnAddPlayers.setOnClickListener(this);
        ImageButton btnClear = (ImageButton) findViewById(R.id.imgBtnClear);
        btnClear.setOnClickListener(this);
        ImageButton btnStartGame = (ImageButton) findViewById(R.id.imgBtnStart);
        btnStartGame.setOnClickListener(this);
		mPlayersList = new ArrayList<>();


        if (savedInstanceState == null || !savedInstanceState.containsKey(PLAYER_KEY)) {
            if (mStored.isPlayersStored()) {
                getPlayersForNewGame();
            } else {
				mPlayersList.add(new Pair<>(1L, new Player(getString(R.string.player_1))));
				mPlayersList.add(new Pair<>(2L, new Player(getString(R.string.player_2))));
            }
        } else {
			ArrayList<Player> tList = savedInstanceState.getParcelableArrayList(PLAYER_KEY);
			mPlayersList = toPairList(tList);
        }


        customListAdapter = new CustomListAdapter(mPlayersList, R.layout.list_players_for_new_game,
				R.id.newPlayerName, true, this);
		mDragList.setLayoutManager(new LinearLayoutManager(this));
        mDragList.setAdapter(customListAdapter, true);
		mDragList.setCanDragHorizontally(false);
    }

    private void getPlayersForNewGame() {
        ArrayList<Player> tList = mStored.loadPlayers(PREFS_NO_DATA);
        if (!tList.isEmpty()) {
			mPlayersList = toPairList(tList);
        }
    }


    private void showPlayerNameDialog(String name) {
        showPlayerNameDialog(name, Sex.MAN);
    }

	private void showPlayerNameDialog(String name, Sex sex) {
        playerDialog = new PlayerNameDialog();
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
			mPlayersList.add(new Pair<>((long) mPlayersList.size(), newPlayer));
		} else {
            Player player = mPlayersList.get(playerIndex).second;
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
		ArrayList<Player> listPlayers = toList();
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
				mPlayersList.clear();
                customListAdapter.notifyDataSetChanged();
                mTracker.send(new HitBuilders.EventBuilder()
                        .setAction("ClearPlayers")
                        .setCategory("Action")
                        .setLabel("MyActivity")
                        .build());
                break;
            case R.id.imgBtnStart:
				int MIN_PLAYER_QUANTITY = 1;
				if (mPlayersList.size() < MIN_PLAYER_QUANTITY) {
                    Toast.makeText(this, getString(R.string.more_players), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, GameActivity.class);
					ArrayList<Player> listPlayers = toList();
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
        Player player = mPlayersList.get(mPosition).second;
        StringBuilder message = new StringBuilder();
        message.append(player.getName())
                .append(EMPTY_STRING)
                .append(getString(R.string.deleted));

        Toast.makeText(this, message.toString(),
                Toast.LENGTH_SHORT
        ).show();

		mPlayersList.remove(mPosition);
        customListAdapter.notifyDataSetChanged();
    }

    public void playerEdit(int playerPosition) {
        Player player = mPlayersList.get(playerPosition).second;
        playerIndex = playerPosition;
        newPlayer = false;
        showPlayerNameDialog(player.getName(), player.getSex());
    }

    public void playerChangeSex(int playerPosition) {
        Player player = mPlayersList.get(playerPosition).second;
        if (Sex.MAN == player.getSex()) {
            player.setSex(Sex.WOMAN);
        } else {
            player.setSex(Sex.MAN);
        }
        customListAdapter.notifyDataSetChanged();
    }

	private ArrayList<Player> toList() {
		ArrayList<Player> listPlayers = new ArrayList<>(mPlayersList.size());
		for (Pair<Long, Player> pair: mPlayersList) {
			listPlayers.add(pair.second);
		}
		return listPlayers;
	}

	private ArrayList<Pair<Long, Player>> toPairList(List<Player> playersList) {
		ArrayList<Pair<Long, Player>> pairList = new ArrayList<>();
		for (int i=0; i < playersList.size(); i++) {
			pairList.add(new Pair<>((long) i, playersList.get(0)));
		}
		return pairList;
	}
}
