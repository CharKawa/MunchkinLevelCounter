package com.g_art.munchkinlevelcounter.bean;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by G_Art on 16/7/2014.
 */
public class Player implements Parcelable, Comparable {

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
    private String name;
    private int level;
    private int gear;
    private int color;
    private boolean winner;
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
        this.winner = false;
        Random rnd = new Random();
        this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public Player(String name, int position) {
        lvlStats = new ArrayList<String>();
        gearStats = new ArrayList<String>();
        powerStats = new ArrayList<String>();
        this.name = name;
        this.level = 1;
        this.gear = 0;
        this.winner = false;
        Random rnd = new Random();
        this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public Player(String name, int lvl, int gear) {
        lvlStats = new ArrayList<String>();
        gearStats = new ArrayList<String>();
        powerStats = new ArrayList<String>();
        this.name = name;
        this.level = lvl;
        this.gear = gear;
        this.winner = false;
        Random rnd = new Random();
        this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

    }

    public Player(Parcel in) {
        lvlStats = new ArrayList<String>();
        gearStats = new ArrayList<String>();
        powerStats = new ArrayList<String>();
        this.name = in.readString();
        this.level = in.readInt();
        this.gear = in.readInt();
        this.color = in.readInt();
        this.winner = in.readByte() != 0;
        in.readStringList(lvlStats);
        in.readStringList(gearStats);
        in.readStringList(powerStats);
    }

    public Player cloneWithoutStats() {
        return new Player(this.name);
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

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
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
        dest.writeByte((byte) (winner ? 1 : 0));
        dest.writeStringList(lvlStats);
        dest.writeStringList(gearStats);
        dest.writeStringList(powerStats);
    }

    @Override
    public int compareTo(Object another) {
        Player pl1 = this;
        Player pl2 = (Player) another;
        Boolean pl1W = pl1.isWinner();
        Boolean pl2W = pl2.isWinner();
        int colorCompare = pl2W.compareTo(pl1W);
        if (colorCompare == 0) {
            return pl1.getLevel() - pl2.getLevel();
        }
        return colorCompare;
    }
}
