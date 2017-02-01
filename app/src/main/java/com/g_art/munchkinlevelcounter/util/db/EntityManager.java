package com.g_art.munchkinlevelcounter.util.db;

import com.g_art.munchkinlevelcounter.model.DaoSession;
import com.g_art.munchkinlevelcounter.model.Game;
import com.g_art.munchkinlevelcounter.model.GameDao;
import com.g_art.munchkinlevelcounter.model.Player;

import java.util.List;

/**
 * Created by agulia on 1/31/17.
 */

public class EntityManager {
	private static EntityManager instance;
	private DaoSession daoSession;

	private EntityManager() {

	}

	public static EntityManager getInstance() {
		if (instance == null) {
			instance = new EntityManager();
		}
		return instance;
	}

	public void init(DaoSession daoSession) {
		this.daoSession = daoSession;
	}

	/**
	 * SAVE
	 **/

	public void savePlayer(Player player) {
		daoSession.getPlayerDao().save(player);
	}

	public void saveGame(Game game) {
		daoSession.getGameDao().save(game);
	}

	/**
	 * LOAD
	 **/

	public Player getPlayerById(Long id) {
		return daoSession.getPlayerDao().load(id);
	}

	public List<Player> getPlayersFromLastGame() {
		return daoSession.getGameDao()
				.queryBuilder()
				.orderDesc(GameDao.Properties.StartDate, GameDao.Properties.EndDate)
				.build()
				.unique()
				.getPlayerList();
	}

	public List<Player> getAllPlayers() {
		return daoSession.getPlayerDao().loadAll();
	}

	public List<Game> getAllPlayedGames() {
		return daoSession.getGameDao().loadAll();
	}

	/**
	 * DELETE
	 **/

	public void deleteGame(Game game) {
		daoSession.getGameDao().delete(game);
	}

	public void deleteGame(Long id) {
		daoSession.getGameDao().deleteByKey(id);
	}

	public void deletePlayer(Player player) {
		daoSession.getPlayerDao().delete(player);
	}

	public void deletePlayer(Long id) {
		daoSession.getPlayerDao().deleteByKey(id);
	}
}
