package com.jvictor01.lobby;

import com.jvictor01.utils.HttpUtils;

import java.util.List;
import java.util.Optional;

public class LobbyService {
    private final HttpUtils httpUtils;

    public LobbyService() {
        this.httpUtils = new HttpUtils();
    }

    public void queue() {
        System.out.println("IN QUEUE:");
        httpUtils.buildPostRequest(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH);
    }


    public void createLobby(LobbySettings lobbySettings) {

        if (QueueIdEnum.getFromId(lobbySettings.getQueueId()) == null) {
            throw new QueueNotSupportedException("Queue id: " + lobbySettings.getQueueId() + "not supported");
        }

        lobbySettings.setAllowablePremadeSizes(
                Optional.ofNullable(lobbySettings.getAllowablePremadeSizes())
                        .orElse(List.of()));

        lobbySettings.setCustomTeam100(
                Optional.ofNullable(lobbySettings.getCustomTeam100())
                        .orElse(List.of()));


        httpUtils.buildPostRequest(LobbyEndpoints.LOBBY_V2, lobbySettings);

    }

    public void updatePositionPreferences(LobbyRoles lobbyRoles) {

        if (lobbyRoles.getFirstPreference() == null || lobbyRoles.getFirstPreference().isBlank()
                || LCULobbyPositionType.UNSELECTED.toString().equals(lobbyRoles.getFirstPreference())) {
            throw new RuntimeException("Primary role empty or unselected");
        }

        httpUtils.buildPutRequest(LobbyEndpoints.POSITION_PREFERENCES, lobbyRoles);

    }


//    public void getLobbyMembers() {
//        List<TeamMember> teamMembers = List.of(httpUtils.buildGetRequestBy(LobbyEndpoints.LOBBY_V2_MEMBERS, TeamMember.class));
//
//    }


    public void playAgain() {
        httpUtils.buildPostRequest(LobbyEndpoints.PLAY_AGAIN);
    }
}
