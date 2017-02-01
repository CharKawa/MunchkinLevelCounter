package com.g_art.munchkinlevelcounter.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;

/**
 * Created by agulia on 1/31/17.
 */

@Entity
public class Game {

	@Id(autoincrement = true)
	private Long id;

	private Date startDate;
	private Date endDate;

	private boolean finished;

	@ToMany
	@JoinEntity(
			entity = PlayerInGameEntity.class,
			sourceProperty = "gameId",
			targetProperty = "playerId"
	)
	private List<Player> playerList;

	/**
	 * Used to resolve relations
	 */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/**
	 * Used for active entity operations.
	 */
	@Generated(hash = 359416843)
	private transient GameDao myDao;

	@Generated(hash = 1882973947)
	public Game(Long id, Date startDate, Date endDate, boolean finished) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.finished = finished;
	}

	@Generated(hash = 380959371)
	public Game() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 1039639504)
	public List<Player> getPlayerList() {
		if (playerList == null) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			PlayerDao targetDao = daoSession.getPlayerDao();
			List<Player> playerListNew = targetDao._queryGame_PlayerList(id);
			synchronized (this) {
				if (playerList == null) {
					playerList = playerListNew;
				}
			}
		}
		return playerList;
	}

	/**
	 * Resets a to-many relationship, making the next get call to query for a fresh result.
	 */
	@Generated(hash = 1310905913)
	public synchronized void resetPlayerList() {
		playerList = null;
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 128553479)
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 1942392019)
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 713229351)
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	public boolean getFinished() {
		return this.finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * called by internal mechanisms, do not call yourself.
	 */
	@Generated(hash = 733596598)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getGameDao() : null;
	}
}
