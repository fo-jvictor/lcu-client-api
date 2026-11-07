package com.jvictor01.chat.dtos;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatParticipant {
    private String puuid;
    private String gameName;
    private String gameTag;
    private String region;

    public List<ChatParticipant> ChatParticipant(JSONArray json) {
        List<ChatParticipant> chatParticipants = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            chatParticipants.add(new ChatParticipant(json.getJSONObject(i)));
        }
        return chatParticipants;
    }

    private ChatParticipant(JSONObject json) {
        this.puuid = json.optString("puuid");
        this.gameName = json.optString("game_name");
        this.gameTag = json.optString("game_tag");
        this.region = json.optString("region");
    }

}
