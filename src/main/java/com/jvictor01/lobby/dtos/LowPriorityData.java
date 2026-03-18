package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LowPriorityData {
    private String bustedLeaverAccessToken;
    private List<Integer> penalizedSummonerIds;
    private int penaltyTime;
    private int penaltyTimeRemaining;
    private String reason;

    public LowPriorityData() {
    }

    public String getBustedLeaverAccessToken() {
        return bustedLeaverAccessToken;
    }

    public void setBustedLeaverAccessToken(String bustedLeaverAccessToken) {
        this.bustedLeaverAccessToken = bustedLeaverAccessToken;
    }

    public List<Integer> getPenalizedSummonerIds() {
        return penalizedSummonerIds;
    }

    public void setPenalizedSummonerIds(List<Integer> penalizedSummonerIds) {
        this.penalizedSummonerIds = penalizedSummonerIds;
    }

    public int getPenaltyTime() {
        return penaltyTime;
    }

    public void setPenaltyTime(int penaltyTime) {
        this.penaltyTime = penaltyTime;
    }

    public int getPenaltyTimeRemaining() {
        return penaltyTimeRemaining;
    }

    public void setPenaltyTimeRemaining(int penaltyTimeRemaining) {
        this.penaltyTimeRemaining = penaltyTimeRemaining;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
