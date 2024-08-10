package com.jvictor01.lobby.team_builder;

import org.json.JSONObject;

public class Action {
    private int actorCellId;
    private int championId;
    private boolean completed;
    private int id;
    private boolean isAllyAction;
    private boolean isInProgress;
    private String type;

    public Action(JSONObject jsonObject) {
        this.actorCellId = jsonObject.getInt("actorCellId");
        this.championId = jsonObject.getInt("championId");
        this.completed = jsonObject.getBoolean("completed");
        this.id = jsonObject.getInt("id");
        this.isAllyAction = jsonObject.getBoolean("isAllyAction");
        this.isInProgress = jsonObject.getBoolean("isInProgress");
        this.type = jsonObject.getString("type");
    }

    public Action() {

    }


    public int getActorCellId() {
        return actorCellId;
    }

    public void setActorCellId(int actorCellId) {
        this.actorCellId = actorCellId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAllyAction() {
        return isAllyAction;
    }

    public void setAllyAction(boolean allyAction) {
        isAllyAction = allyAction;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
