package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Member {

    private boolean allowedChangeActivity;
    private boolean allowedInviteOthers;
    private boolean allowedKickOthers;
    private boolean allowedStartActivity;
    private boolean allowedToggleInvite;

    private boolean autoFillEligible;
    private boolean autoFillProtectedForPromos;
    private boolean autoFillProtectedForRemedy;
    private boolean autoFillProtectedForSoloing;
    private boolean autoFillProtectedForStreaking;

    private int botChampionId;
    private String botDifficulty;
    private String botId;
    private String botPosition;
    private String botUuid;

    //can be converted to PositionsPreference object or PlayerSlots
    private String firstPositionPreference;
    private String secondPositionPreference;
    //ThirdPosition until fifth will be empty most of the time
    private String thirdPositionPreference;
    private String fourthPositionPreference;
    private String fifthPositionPreference;
    private String intraSubteamPosition;

    private boolean isBot;
    private boolean isLeader;
    private boolean isSpectator;

    private Object memberData;
    private List<Object> playerSlots;

    private String puuid;
    private boolean ready;
    private boolean showGhostedBanner;

    private Integer strawberryMapId;
    private Integer subteamIndex;

    private int summonerIconId;
    private long summonerId;

    //summonerInternalName and summonerName will be empty most of the time
    private String summonerInternalName;
    private int summonerLevel;
    private String summonerName;

    private int teamId;

}