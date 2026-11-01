package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GameConfig {
    private List<Integer> allowablePremadeSizes;
    private String customLobbyName;
    private String customMutatorName;
    private List<Object> customRewardsDisabledReasons;
    private String customSpectatorPolicy;
    private List<Object> customSpectators;
    private List<Object> customTeam100;
    private List<Object> customTeam200;
    private String gameMode;
    private boolean isCustom;
    private boolean isLobbyFull;
    private boolean isTeamBuilderManaged;
    private int mapId;
    private int maxHumanPlayers;
    private int maxLobbySize;
    private int maxLobbySpectatorCount;
    private int maxTeamSize;
    private int numPlayersPerTeam;
    private int numberOfTeamsInLobby;
    private String pickType;
    private boolean premadeSizeAllowed;
    private int queueId;
    private boolean shouldForceScarcePositionSelection;
    private boolean showPositionSelector;
    private boolean showQuickPlaySlotSelection;

}
