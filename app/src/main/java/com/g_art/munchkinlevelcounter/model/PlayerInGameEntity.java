package com.g_art.munchkinlevelcounter.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by agulia on 1/31/17.
 */

@Entity
public class PlayerInGameEntity {
	@Id
	private Long id;

	private Long playerId;
	private Long gameId;

	@Generated(hash = 1177738851)
	public PlayerInGameEntity(Long id, Long playerId, Long gameId) {
		this.id = id;
		this.playerId = playerId;
		this.gameId = gameId;
	}

	@Generated(hash = 1767035566)
	public PlayerInGameEntity() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Long getGameId() {
		return this.gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
}
