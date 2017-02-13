package com.g_art.munchkinlevelcounter.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.g_art.munchkinlevelcounter.util.db.converters.GenderConverter;
import com.g_art.munchkinlevelcounter.util.db.converters.StatsConverter;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by G_Art on 16/7/2014.
 */
@Entity
public class Player implements Parcelable, Comparable<Player> {

	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
		public Player createFromParcel(Parcel in) {
			return new Player(in);
		}

		public Player[] newArray(int size) {
			return new Player[size];
		}
	};

	@Id(autoincrement = true)
	private long id;

	@Index
	private String name;
	private int level;
	private int gear;
	private int mods;
	private int color;
	private boolean winner;
	@Convert(converter = GenderConverter.class, columnType = Integer.class)
	private Gender gender;
	@Convert(converter = StatsConverter.class, columnType = String.class)
	private ArrayList<String> lvlStats;
	@Convert(converter = StatsConverter.class, columnType = String.class)
	private ArrayList<String> gearStats;
	@Convert(converter = StatsConverter.class, columnType = String.class)
	private ArrayList<String> powerStats;

	@ToMany
	@JoinEntity(
			entity = PlayerInGameEntity.class,
			sourceProperty = "playerId",
			targetProperty = "gameId"
	)
	private List<Game> games;

	/**
	 * Used to resolve relations
	 */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/**
	 * Used for active entity operations.
	 */
	@Generated(hash = 2108114900)
	private transient PlayerDao myDao;

	public Player() {
	}

	public Player(String name) {
		lvlStats = new ArrayList<>();
		gearStats = new ArrayList<>();
		powerStats = new ArrayList<>();
		this.name = name;
		this.level = 1;
		this.gear = 0;
		this.gender = Gender.MAN;
		this.winner = false;
		generateColor();
	}

	public Player(String name, int lvl, int gear) {
		lvlStats = new ArrayList<>();
		gearStats = new ArrayList<>();
		powerStats = new ArrayList<>();
		this.name = name;
		this.level = lvl;
		this.gear = gear;
		this.winner = false;
		this.gender = Gender.MAN;
		generateColor();
	}

	public Player(String name, int level, int gear, Gender gender) {
		this.name = name;
		this.level = level;
		this.gear = gear;
		this.gender = gender;
	}

	public Player(Parcel in) {
		lvlStats = new ArrayList<>();
		gearStats = new ArrayList<>();
		powerStats = new ArrayList<>();
		this.name = in.readString();
		this.level = in.readInt();
		this.gear = in.readInt();
		this.color = in.readInt();
		this.gender = (Gender) in.readSerializable();
		this.winner = in.readByte() != 0;
		in.readStringList(lvlStats);
		in.readStringList(gearStats);
		in.readStringList(powerStats);
	}

	@Generated(hash = 1649594086)
	public Player(long id, String name, int level, int gear, int mods, int color,
				  boolean winner, Gender gender, ArrayList<String> lvlStats,
				  ArrayList<String> gearStats, ArrayList<String> powerStats) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.gear = gear;
		this.mods = mods;
		this.color = color;
		this.winner = winner;
		this.gender = gender;
		this.lvlStats = lvlStats;
		this.gearStats = gearStats;
		this.powerStats = powerStats;
	}

	public void incrementLvl() {
		this.level++;
	}

	public void decrementLvl() {
		this.level--;
	}

	public void incrementGear() {
		this.gear++;
	}

	public void decrementGear() {
		this.gear--;
	}

	public void toggleGender() {
		if (this.getGender() == Gender.MAN) {
			this.setGender(Gender.WOMAN);
		} else {
			this.setGender(Gender.MAN);
		}
	}

	public Player cloneWithoutStats() {
		Player newPlayer = new Player(this.name);
		newPlayer.setGender(this.getGender());
		return newPlayer;
	}

	public Player cloneForBattle() {
		return new Player(this.name, this.level, this.gear, this.gender);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getGear() {
		return gear;
	}

	public void setGear(int gear) {
		this.gear = gear;
	}

	public int getMods() {
		return mods;
	}

	public void setMods(int mods) {
		this.mods = mods;
	}

	public boolean isWinner() {
		return winner;
	}

	public ArrayList<String> getLvlStats() {
		return lvlStats;
	}

	public void setLvlStats(ArrayList<String> lvlStats) {
		this.lvlStats = lvlStats;
	}

	public ArrayList<String> getGearStats() {
		return gearStats;
	}

	public void setGearStats(ArrayList<String> gearStats) {
		this.gearStats = gearStats;
	}

	public ArrayList<String> getPowerStats() {
		return powerStats;
	}

	public void setPowerStats(ArrayList<String> powerStats) {
		this.powerStats = powerStats;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public long getId() {
		return id;
	}

	public Player setId(long id) {
		this.id = id;
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Player{" +
				"name='" + name + '\'' +
				", level=" + level +
				", gear=" + gear +
				", isWinner=" + winner +
				", lvlStats=" + lvlStats +
				", gearStats=" + gearStats +
				", powerStats=" + powerStats +
				", gender=" + gender.toString() +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player)) return false;

		Player player = (Player) o;

		if (gear != player.gear) return false;
		if (level != player.level) return false;
		if (color != player.color) return false;
		if (winner != player.winner) return false;
		if (!name.equals(player.name)) return false;
		if (!gender.equals(player.gender)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + level;
		result = 31 * result + gear;
		result = 31 * result + (winner ? 1 : 0);
		return result;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(level);
		dest.writeInt(gear);
		dest.writeInt(color);
		dest.writeSerializable(gender);
		dest.writeByte((byte) (winner ? 1 : 0));
		dest.writeStringList(lvlStats);
		dest.writeStringList(gearStats);
		dest.writeStringList(powerStats);
	}

	@Override
	public int compareTo(@NonNull Player another) {
		Player pl1 = this;
		Boolean pl1W = pl1.isWinner();
		Boolean pl2W = another.isWinner();
		int colorCompare = pl2W.compareTo(pl1W);
		if (colorCompare == 0) {
			return pl1.getLevel() - another.getLevel();
		}
		return colorCompare;
	}

	public String getHelperInfo(String template) {
		return String.format(template, this.getPower(), this.getName(), this.getLevel());
	}

	public int getPower() {
		return this.level + this.gear;
	}

	public void generateColor() {
		final Random rnd = new Random();
		setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
	}

	public boolean getWinner() {
		return this.winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 471773044)
	public List<Game> getGames() {
		if (games == null) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			GameDao targetDao = daoSession.getGameDao();
			List<Game> gamesNew = targetDao._queryPlayer_Games(id);
			synchronized (this) {
				if (games == null) {
					games = gamesNew;
				}
			}
		}
		return games;
	}

	/**
	 * Resets a to-many relationship, making the next get call to query for a fresh result.
	 */
	@Generated(hash = 1596068969)
	public synchronized void resetGames() {
		games = null;
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

	/**
	 * called by internal mechanisms, do not call yourself.
	 */
	@Generated(hash = 1600887847)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getPlayerDao() : null;
	}
}
