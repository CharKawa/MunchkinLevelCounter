package com.g_art.munchkinlevelcounter.model;

import com.google.gson.annotations.SerializedName;

public enum Gender {
	@SerializedName("0")
	MAN(0),
	@SerializedName("1")
	WOMAN(1);

	private final int type;

	Gender(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
