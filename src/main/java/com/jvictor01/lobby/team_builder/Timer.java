package com.jvictor01.lobby.team_builder;

import org.json.JSONObject;

public class Timer {
    private int adjustedTimeLeftInPhase;
    private int totalTimeInPhase;
    private String phase;
    private boolean isInfinite;
    private long internalNowInEpochMs;

    public Timer(JSONObject jsonObject) {
        this.adjustedTimeLeftInPhase = jsonObject.getInt("adjustedTimeLeftInPhase");
        this.totalTimeInPhase = jsonObject.getInt("totalTimeInPhase");
        this.phase = jsonObject.getString("phase");
        this.isInfinite = jsonObject.getBoolean("isInfinite");
        this.internalNowInEpochMs = jsonObject.getLong("internalNowInEpochMs");
    }

    public Timer() {

    }

    public void setAdjustedTimeLeftInPhase(int adjustedTimeLeftInPhase) {
        this.adjustedTimeLeftInPhase = adjustedTimeLeftInPhase;
    }

    public void setTotalTimeInPhase(int totalTimeInPhase) {
        this.totalTimeInPhase = totalTimeInPhase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void setInfinite(boolean infinite) {
        isInfinite = infinite;
    }

    public void setInternalNowInEpochMs(long internalNowInEpochMs) {
        this.internalNowInEpochMs = internalNowInEpochMs;
    }

    public int getAdjustedTimeLeftInPhase() {
        return adjustedTimeLeftInPhase;
    }

    public int getTotalTimeInPhase() {
        return totalTimeInPhase;
    }

    public String getPhase() {
        return phase;
    }

    public boolean isInfinite() {
        return isInfinite;
    }

    public long getInternalNowInEpochMs() {
        return internalNowInEpochMs;
    }
}
