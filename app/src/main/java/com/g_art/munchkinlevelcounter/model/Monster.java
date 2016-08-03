package com.g_art.munchkinlevelcounter.model;

/**
 * MunchkinLevelCounter
 * Created by fftem on 03-Aug-16.
 */

public class Monster extends BaseModel {
	private final static String MONSTER_NAME = "Monster";

	public Monster() {
		this.name = MONSTER_NAME;
		this.level = 1;
		this.mods = 0;
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

	public int getMods() {
		return mods;
	}

	public void setMods(int mods) {
		this.mods = mods;
	}
}
