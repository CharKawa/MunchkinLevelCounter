package com.g_art.munchkinlevelcounter.util.db.converters;

import com.g_art.munchkinlevelcounter.util.StoredPlayers;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;

/**
 * Created by agulia on 1/31/17.
 */
public class StatsConverter implements PropertyConverter<ArrayList<String>, String> {

	@Override
	public ArrayList<String> convertToEntityProperty(String databaseValue) {
		if (databaseValue == null || databaseValue.isEmpty()) {
			return new ArrayList<>();
		}
		return StoredPlayers.deSerialiseStats(databaseValue);
	}

	@Override
	public String convertToDatabaseValue(ArrayList<String> entityProperty) {
		if (entityProperty == null) {
			return null;
		}
		return StoredPlayers.serialiseStats(entityProperty);
	}
}