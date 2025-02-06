package com.jvictor01.lobby;

import com.jvictor01.summoners.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LobbyService {
    private final HttpUtils httpUtils;
    private final SummonerService summonerService;

    public LobbyService() {
        this.httpUtils = new HttpUtils();
        this.summonerService = new SummonerService();
    }

    public HttpResponse<String> matchmakingSearch() {
        System.out.println("IN QUEUE:");
        return httpUtils.buildPostRequest(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH);
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

    public HttpResponse<String> kickSummonerBySummonerId(String summonerId) {
        String formattedUri = String.format(LobbyEndpoints.KICK_SUMMONER_BY_SUMMONER_ID, summonerId);
        return httpUtils.buildPostRequest(formattedUri);
    }

    public void getLobbyMembers() {

        List<Invitation> invitations = httpUtils.getLobbyInvitations(LobbyEndpoints.INVITATIONS_V2);


    }

    public HttpResponse<String> postInvitationByNickname(String nickname, String tag) {
        String puuid = summonerService.getSummonerPuuidByNicknameAndTagLine(nickname, tag);
        Summoner summoner = summonerService.getSummonerByPuuid(puuid);

        Invitation invitation = new Invitation();
        invitation.setToSummonerId(summoner.getSummonerId());
        invitation.setToSummonerName(summoner.getGameName());

        return httpUtils.buildPostRequest(LobbyEndpoints.INVITATIONS_V2, Collections.singletonList(invitation));
    }


}
