package com.jvictor01.lobby.team_builder.dtos;

import org.json.JSONObject;

public class MucJwtDto {
    private String jwt;
    private String channelClaim;
    private String domain;
    private String targetRegion;

    public MucJwtDto(JSONObject jsonObject) {
        this.jwt = jsonObject.getString("jwt");
        this.channelClaim = jsonObject.getString("channelClaim");
        this.domain = jsonObject.getString("domain");
        this.targetRegion = jsonObject.getString("targetRegion");
    }

    public MucJwtDto() {

    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setChannelClaim(String channelClaim) {
        this.channelClaim = channelClaim;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setTargetRegion(String targetRegion) {
        this.targetRegion = targetRegion;
    }

    public String getJwt() {
        return jwt;
    }

    public String getChannelClaim() {
        return channelClaim;
    }

    public String getDomain() {
        return domain;
    }

    public String getTargetRegion() {
        return targetRegion;
    }
}
