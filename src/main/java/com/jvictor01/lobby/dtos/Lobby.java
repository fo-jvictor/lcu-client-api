package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jvictor01.lobby.team_builder.dtos.MucJwtDto;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Lobby {
    private boolean canStartActivity;
    private GameConfig gameConfig;
    private List<Invitation> invitations;
    private Member localMember;
    private List<Member> members;
    private MucJwtDto mucJwtDto;
    private String multiUserChatId;
    private String multiUserChatPassword;
    private String partyId;
    private String partyType;
    private List<Object> popularChampions;
    private List<Object> restrictions;
    private List<String> scarcePositions;
    private List<Object> warnings;
}
