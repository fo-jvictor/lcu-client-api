package com.jvictor01.lobby.team_builder.dtos;

import org.json.JSONObject;

public class BenchChampion {
    private int championId;
    private boolean isPriority;

    public BenchChampion(JSONObject jsonObject) {
        this.championId = jsonObject.getInt("championId");
        this.isPriority = jsonObject.getBoolean("isPriority");
    }

    public BenchChampion() {

    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }

    public int getChampionId() {
        return championId;
    }

    public boolean isPriority() {
        return isPriority;
    }
}
