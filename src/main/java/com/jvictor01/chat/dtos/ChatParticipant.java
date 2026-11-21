package com.jvictor01.chat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.json.JSONObject;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatParticipant {
    private String cid;
    private String gameName;
    private String gameTag;
    private String puuid;
    private String region;

    public ChatParticipant(JSONObject json) {
        this.cid = json.optString("cid");
        this.puuid = json.optString("puuid");
        this.gameName = json.optString("game_name");
        this.gameTag = json.optString("game_tag");
        this.region = json.optString("region");
    }

}
