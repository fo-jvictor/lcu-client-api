package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class Invitation {
    private String invitationId;
    private String invitationType;
    private String state;
    private String timestamp;
    private String toPuuid;
    private long toSummonerId;
    private String toSummonerName;

    public Invitation() {

    }
}
