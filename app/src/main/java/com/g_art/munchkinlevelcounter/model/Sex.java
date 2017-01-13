package com.g_art.munchkinlevelcounter.model;

import com.google.gson.annotations.SerializedName;

/**
 * MunchkinLevelCounter
 * Created by fftem on 27-Apr-16.
 */
public enum Sex {
	@SerializedName("0")
	MAN(0),
	@SerializedName("1")
	WOMAN(1);

	private int sex;

	Sex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return sex;
	}
}
