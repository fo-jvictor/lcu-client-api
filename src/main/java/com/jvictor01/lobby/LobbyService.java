package com.jvictor01.lobby;

import com.jvictor01.summoners.Summoner;
import com.jvictor01.summoners.SummonerNotFoundException;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class LobbyService {
    private final HttpUtils httpUtils;
    private final SummonerService summonerService;

    public LobbyService() {
        this.httpUtils = new HttpUtils();
        this.summonerService = new SummonerService();
    }

    public void matchmakingSearch() {
        System.out.println("IN QUEUE:");
        httpUtils.buildPostRequest(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH);
    }

    public HttpResponse<String> cancelMatchmakingSearch() {
        return httpUtils.buildDeleteRequest(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH);
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


    public void getLobbyMembers() {

        List<Invitation> invitations = httpUtils.getLobbyInvitations(LobbyEndpoints.INVITATIONS_V2);


    }

    public void postInvitationByNickname(String nickname) {
        Summoner[] summoners = summonerService.getSummonerDetailsByNickname(nickname);

        if (summoners[0] != null) {
            Invitation invitation = new Invitation();
            invitation.setToSummonerId(summoners[0].getSummonerId());
            invitation.setToSummonerName(summoners[0].getGameName());

            httpUtils.buildPostRequest(LobbyEndpoints.INVITATIONS_V2, invitation);
        }

        throw new SummonerNotFoundException("Summoner not found by nickname: " + nickname);

    }


    public void playAgain() {
        httpUtils.buildPostRequest(LobbyEndpoints.PLAY_AGAIN);
    }
}
