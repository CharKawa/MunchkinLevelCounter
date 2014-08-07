package com.g_art.munchkinlevelcounter.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by G_Art on 16/7/2014.
 */
public class Player implements Parcelable {

    private String name;
    private int level;
    private int gear;
    private ArrayList<String> lvlStats;
    private ArrayList<String> gearStats;
    private ArrayList<String> powerStats;

    public Player() {
    }

    public Player(String name) {
        lvlStats = new ArrayList<String>();
        gearStats = new ArrayList<String>();
        powerStats = new ArrayList<String>();
        this.name = name;
        this.level = 1;
        this.gear = 0;
    }

    public Player(String name, int lvl, int gear) {
        lvlStats = new ArrayList<String>();
        gearStats = new ArrayList<String>();
        powerStats = new ArrayList<String>();
        this.name = name;
        this.level = lvl;
        this.gear = gear;
    }

    public Player(Parcel in) {
        lvlStats = new ArrayList<String>();
        gearStats = new ArrayList<String>();
        powerStats = new ArrayList<String>();
        this.name = in.readString();
        this.level = in.readInt();
        this.gear = in.readInt();
        in.readStringList(lvlStats);
        in.readStringList(gearStats);
        in.readStringList(powerStats);
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

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", gear=" + gear +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (gear != player.gear) return false;
        if (level != player.level) return false;
        if (!name.equals(player.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + level;
        result = 31 * result + gear;
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
        dest.writeStringList(lvlStats);
        dest.writeStringList(gearStats);
        dest.writeStringList(powerStats);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
