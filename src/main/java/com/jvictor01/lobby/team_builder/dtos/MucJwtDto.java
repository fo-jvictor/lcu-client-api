package com.jvictor01.lobby.team_builder.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.json.JSONObject;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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

}
