package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestedPlayer {
    private String reason;
    private Long summonerId;
    private String summonerName;

    public SuggestedPlayer(String reason, Long summonerId, String summonerName) {
        this.reason = reason;
        this.summonerId = summonerId;
        this.summonerName = summonerName;
    }

    public SuggestedPlayer(){}

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(Long summonerId) {
        this.summonerId = summonerId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }
}
