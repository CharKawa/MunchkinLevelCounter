package com.g_art.munchkinlevelcounter.bean;

/**
 * MunchkinLevelCounter
 * Created by fftem on 27-Apr-16.
 */
public enum Sex {
    MAN(0),
    WOMAN(1);

    private int sexType;

    Sex(int sexType) {
        this.sexType = sexType;
    }

    public int getSexType() {
        return sexType;
    }

    @Override
    public String toString() {
        return "Sex{" +
                "sexType=" + sexType +
                '}';
    }
}
