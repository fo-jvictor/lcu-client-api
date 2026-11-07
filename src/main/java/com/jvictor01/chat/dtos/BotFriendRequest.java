package com.jvictor01.chat.dtos;

import lombok.Getter;

@Getter
public class BotFriendRequest {
    private String puuid;
    private String gameName;
    private String tagLine;
    private Long summonerLevel;
    private BotReason reason;

    public BotFriendRequest(FriendRequest friendRequest, Long summonerLevel, BotReason reason) {
        this.puuid = friendRequest.getPuuid();
        this.gameName = friendRequest.getGameName();
        this.tagLine = friendRequest.getTagLine();
        this.summonerLevel = summonerLevel;
        this.reason = reason;
    }
}
