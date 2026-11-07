package com.jvictor01.chat.dtos;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class FriendRequest {
    public static final String INCOMING = "in";

    private String puuid;
    private String gameName;
    private String tagLine;
    private String direction;

    public FriendRequest(JSONObject json) {
        this.puuid = json.optString("puuid");
        this.gameName = json.optString("gameName");
        this.tagLine = json.optString("tagLine");
        this.direction = json.optString("direction");
    }

    public boolean isIncoming() {
        return INCOMING.equalsIgnoreCase(direction);
    }
}
