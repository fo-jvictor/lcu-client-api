package com.jvictor01.lobby;

public class Invitation {
    private String invitationId;
    private String invitationType;
    private String state;
    private String timestamp;
    private long toSummonerId;
    private String toSummonerName;

    public Invitation() {

    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public void setInvitationType(String invitationType) {
        this.invitationType = invitationType;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setToSummonerId(long toSummonerId) {
        this.toSummonerId = toSummonerId;
    }

    public void setToSummonerName(String toSummonerName) {
        this.toSummonerName = toSummonerName;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public String getInvitationType() {
        return invitationType;
    }

    public String getState() {
        return state;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getToSummonerId() {
        return toSummonerId;
    }

    public String getToSummonerName() {
        return toSummonerName;
    }
}
