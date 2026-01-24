package com.jvictor01.lobby.dtos;

import org.json.JSONObject;

public class LobbySettings {
    private int queueId;

    public LobbySettings(JSONObject json) {
        this.queueId = json.getInt("queueId");
    }

    public LobbySettings() {

    }

    public int getQueueId() {
        return queueId;
    }
}
