package com.jvictor01.lobby;

import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.lobby.dtos.Invitation;
import com.jvictor01.lobby.dtos.LobbyRoles;
import com.jvictor01.lobby.dtos.LobbySettings;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.summoners.dtos.Summoner;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LobbyService {
    private final HttpWebClient httpWebClient;
    private final SummonerService summonerService;

    public LobbyService() {
        this.httpWebClient = new HttpWebClient();
        this.summonerService = new SummonerService();
    }

    public HttpResponse<String> matchmakingSearch() {
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH, HttpMethods.POST);
    }

    public HttpResponse<String> cancelMatchmakingSearch() {
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH, HttpMethods.DELETE);
    }


    public HttpResponse<String> createLobby(LobbySettings lobbySettings) {

        if (QueueIdEnum.getFromId(lobbySettings.getQueueId()) == null) {
            throw new QueueNotSupportedException("Queue id: " + lobbySettings.getQueueId() + "not supported");
        }

        lobbySettings.setAllowablePremadeSizes(
                Optional.ofNullable(lobbySettings.getAllowablePremadeSizes())
                        .orElse(List.of()));

        lobbySettings.setCustomTeam100(
                Optional.ofNullable(lobbySettings.getCustomTeam100())
                        .orElse(List.of()));

        return httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2, HttpMethods.POST, lobbySettings);

    }

    public HttpResponse<String> updatePositionPreferences(LobbyRoles lobbyRoles) {

        if (lobbyRoles.getFirstPreference() == null || lobbyRoles.getFirstPreference().isBlank()
                || LCULobbyPositionType.UNSELECTED.toString().equals(lobbyRoles.getFirstPreference())) {
            throw new RuntimeException("Primary role empty or unselected");
        }

        return httpWebClient.buildRequestForLcu(LobbyEndpoints.POSITION_PREFERENCES, HttpMethods.PUT, lobbyRoles);
    }

    public HttpResponse<String> kickSummonerBySummonerId(String summonerId) {
        String formattedUri = String.format(LobbyEndpoints.KICK_SUMMONER_BY_SUMMONER_ID, summonerId);
        return httpWebClient.buildRequestForLcu(formattedUri, HttpMethods.POST);
    }

    public HttpResponse<String> postInvitationByNickname(String nickname, String tag) {
        String puuid = summonerService.getSummonerPuuidByNicknameAndTagLine(nickname, tag);
        Summoner summoner = summonerService.getSummonerByPuuid(puuid);

        Invitation invitation = new Invitation();
        invitation.setToSummonerId(summoner.getSummonerId());
        invitation.setToSummonerName(summoner.getGameName());

        return httpWebClient.buildRequestForLcu(LobbyEndpoints.INVITATIONS_V2, HttpMethods.POST, Collections.singletonList(invitation));
    }


}
