package com.g_art.munchkinlevelcounter.util.db.converters;

import com.g_art.munchkinlevelcounter.model.Gender;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by agulia on 1/31/17.
 */


public class GenderConverter implements PropertyConverter<Gender, Integer> {

	@Override
	public Gender convertToEntityProperty(Integer databaseValue) {
		if (databaseValue == null) {
			return Gender.MAN;
		}
		for (Gender gender : Gender.values()) {
			if (gender.getType() == databaseValue) {
				return gender;
			}
		}
		return Gender.MAN;
	}

	@Override
	public Integer convertToDatabaseValue(Gender entityProperty) {
		return entityProperty == null ? Gender.MAN.getType() : entityProperty.getType();
	}
}