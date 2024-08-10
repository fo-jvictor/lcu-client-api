package com.jvictor01.summoners;

import org.json.JSONObject;

public class RerollPoints {
    private int currentPoints;
    private int maxRolls;
    private int numberOfRolls;
    private int pointsCostToRoll;
    private int pointsToReroll;

    public RerollPoints(JSONObject jsonObject) {
        this.currentPoints = jsonObject.getInt("currentPoints");
        this.maxRolls = jsonObject.getInt("maxRolls");
        this.numberOfRolls = jsonObject.getInt("numberOfRolls");
        this.pointsCostToRoll = jsonObject.getInt("pointsCostToRoll");
        this.pointsToReroll = jsonObject.getInt("pointsToReroll");
    }

    public RerollPoints() {
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public int getMaxRolls() {
        return maxRolls;
    }

    public int getNumberOfRolls() {
        return numberOfRolls;
    }

    public int getPointsCostToRoll() {
        return pointsCostToRoll;
    }

    public int getPointsToReroll() {
        return pointsToReroll;
    }
    @Override
    public String toString() {
        return "RerollPoints{" +
                "currentPoints=" + currentPoints +
                ", maxRolls=" + maxRolls +
                ", numberOfRolls=" + numberOfRolls +
                ", pointsCostToRoll=" + pointsCostToRoll +
                ", pointsToReroll=" + pointsToReroll +
                '}';
    }
}