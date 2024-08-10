package com.jvictor01.lobby;

import org.json.JSONObject;

public class LobbyRoles {
    private String firstPreference;
    private String secondPreference;

    public LobbyRoles(JSONObject jsonObject) {
        this.firstPreference = jsonObject.getString("firstPreference");
        this.secondPreference = jsonObject.getString("secondPreference");
    }

    public LobbyRoles() {
    }

    public void setFirstPreference(String firstPreference) {
        this.firstPreference = firstPreference;
    }

    public void setSecondPreference(String secondPreference) {
        this.secondPreference = secondPreference;
    }

    public String getFirstPreference() {
        return firstPreference;
    }

    public String getSecondPreference() {
        return secondPreference;
    }
}
