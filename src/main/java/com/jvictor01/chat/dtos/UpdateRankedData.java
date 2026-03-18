package com.jvictor01.chat.dtos;

import org.json.JSONObject;

public class UpdateRankedData {
    private RankedLeagueQueue rankedLeagueQueue;
    private RankedLeagueTier rankedLeagueTier;
    private RankedLeagueDivision rankedLeagueDivision;

    public UpdateRankedData(JSONObject json) {
        this.rankedLeagueQueue = RankedLeagueQueue.valueOf(json.getString("rankedLeagueQueues"));
        this.rankedLeagueTier = RankedLeagueTier.valueOf(json.getString("rankedLeagueTier"));
        this.rankedLeagueDivision = RankedLeagueDivision.valueOf(json.getString("rankedLeagueDivision"));
    }

    public RankedLeagueQueue getRankedLeagueQueues() {
        return rankedLeagueQueue;
    }

    public void setRankedQueues(RankedLeagueQueue rankedQueues) {
        this.rankedLeagueQueue = rankedQueues;
    }

    public RankedLeagueTier getRankedLeagueTier() {
        return rankedLeagueTier;
    }

    public void setRankedLeagueTier(RankedLeagueTier rankedLeagueTier) {
        this.rankedLeagueTier = rankedLeagueTier;
    }

    public RankedLeagueDivision getRankedLeagueDivision() {
        return rankedLeagueDivision;
    }

    public void setRankedLeagueDivision(RankedLeagueDivision rankedLeagueDivision) {
        this.rankedLeagueDivision = rankedLeagueDivision;
    }
}
