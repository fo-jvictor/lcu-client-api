package com.jvictor01.lobby;

import com.jvictor01.lobby.dtos.Invitation;
import com.jvictor01.lobby.dtos.LobbyRoles;
import com.jvictor01.lobby.dtos.LobbySettings;
import com.jvictor01.summoners.dtos.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;
import java.util.ArrayList;
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
        return httpUtils.buildPostRequest(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH);
    }

    public HttpResponse<String> cancelMatchmakingSearch() {
        return httpUtils.buildDeleteRequest(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH);
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


        return httpUtils.buildPostRequest(LobbyEndpoints.LOBBY_V2, lobbySettings);

    }

    public HttpResponse<String> updatePositionPreferences(LobbyRoles lobbyRoles) {

        if (lobbyRoles.getFirstPreference() == null || lobbyRoles.getFirstPreference().isBlank()
                || LCULobbyPositionType.UNSELECTED.toString().equals(lobbyRoles.getFirstPreference())) {
            throw new RuntimeException("Primary role empty or unselected");
        }

        return httpUtils.buildPutRequest(LobbyEndpoints.POSITION_PREFERENCES, lobbyRoles);

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

    public HttpResponse<String> getLobbys() {
        return httpUtils.getLobbys(LobbyEndpoints.GAME_QUEUES);
    }

    public HttpResponse<String> postCustomInvitation() {
        List<String> nicknamesAndTag = List.of("TheNoob45#br1", "BrunnoHG#br1",
                "Darth Nebro#sith", "Pipo#4117", "uCaule#br1", "dév#GOAT1",
                "Techy#6925", "Techy#777", "não sou egirl#TWT");

        List<Invitation> invitations = new ArrayList<>();

        nicknamesAndTag.forEach(curretNicknameAndTag -> {
            Summoner[] summonerDetailsByNickname = summonerService.getSummonerDetailsByNickname(curretNicknameAndTag);
            Summoner summoner = summonerDetailsByNickname[0];
            Invitation invitation = new Invitation();
            invitation.setToSummonerId(summoner.getSummonerId());
            invitation.setToSummonerName(summoner.getGameName());
            invitations.add(invitation);
        });

        return httpUtils.buildPostRequest(LobbyEndpoints.INVITATIONS_V2, invitations);
    }


}
