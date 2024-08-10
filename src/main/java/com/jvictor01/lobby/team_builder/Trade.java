package com.jvictor01.lobby.team_builder;

import org.json.JSONObject;

public class Trade {
    private int id;
    private int cellId;
    private String state;

    public Trade(JSONObject jsonObject) {
        this.id = jsonObject.getInt("id");
        this.cellId = jsonObject.getInt("cellId");
        this.state = jsonObject.getString("state");
    }

    public Trade() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getCellId() {
        return cellId;
    }

    public String getState() {
        return state;
    }
}
