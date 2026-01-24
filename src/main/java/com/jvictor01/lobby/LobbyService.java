package com.jvictor01.lobby;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.http.Response;
import com.jvictor01.lobby.dtos.GameQueue;
import com.jvictor01.lobby.dtos.Invitation;
import com.jvictor01.lobby.dtos.LobbyRoles;
import com.jvictor01.lobby.dtos.LobbySettings;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.summoners.dtos.Summoner;
import org.json.JSONObject;

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
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2, HttpMethods.POST, lobbySettings);
    }

    public Response<String> getCurrentLobbyFriendlyName() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2, HttpMethods.GET);
        JSONObject gameConfig = new JSONObject(response.body()).getJSONObject("gameConfig");
        Integer queueId = (Integer) gameConfig.get("queueId");

        String friendlyLobbyName = Optional.ofNullable(QueueIdEnum.getFromId(queueId))
                .map(QueueIdEnum::getFriendlyQueueName)
                .orElse(getLobbyShortNameByQueueId(queueId));

        return new Response<>(response.statusCode(), friendlyLobbyName);
    }

    public HttpResponse<String> updatePositionPreferences(LobbyRoles lobbyRoles) {
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.POSITION_PREFERENCES, HttpMethods.PUT, lobbyRoles);
    }

    public HttpResponse<String> kickSummonerBySummonerId(String summonerId) {
        String formattedUri = String.format(LobbyEndpoints.KICK_SUMMONER_BY_SUMMONER_ID, summonerId);
        return httpWebClient.buildRequestForLcu(formattedUri, HttpMethods.POST);
    }

    public Response<List<GameQueue>> getAvailableGameQueues() {

        HttpResponse<String> response =
                httpWebClient.buildRequestForLcu(LobbyEndpoints.GAME_QUEUES, HttpMethods.GET);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<GameQueue> queues = mapper.readValue(response.body(), new TypeReference<>() {
            });

            List<GameQueue> availableGameQueues = queues.stream()
                    .filter(GameQueue::isEnabled)
                    .filter(GameQueue::isVisible)
                    .filter(gameQueue -> !gameQueue.isCustom())
                    .filter(gameQueue -> !gameQueue.getCategory().equalsIgnoreCase("Custom"))
                    .filter(gameQueue -> !gameQueue.getGameSelectCategory().equalsIgnoreCase("CreateCustom"))
                    .filter(gameQueue -> !gameQueue.getQueueAvailability().equalsIgnoreCase("PlatformDisabled"))
                    .filter(gameQueue -> !gameQueue.isRemovalFromGameAllowed())
                    .toList();

            return new Response<>(response.statusCode(), availableGameQueues);

        } catch (Exception e) {
            return new Response<>(500, null);
        }
    }


    public HttpResponse<String> postInvitationByNickname(String nickname, String tag) {
        String puuid = summonerService.getSummonerPuuidByNicknameAndTagLine(nickname, tag);
        Summoner summoner = summonerService.getSummonerByPuuid(puuid);

        Invitation invitation = new Invitation();
        invitation.setToSummonerId(summoner.getSummonerId());
        invitation.setToSummonerName(summoner.getGameName());

        return httpWebClient.buildRequestForLcu(LobbyEndpoints.INVITATIONS_V2, HttpMethods.POST, Collections.singletonList(invitation));
    }

    private String getLobbyShortNameByQueueId(int queueId) {
        String body = httpWebClient.buildRequestForLcu(String.format(LobbyEndpoints.GAME_QUEUE_BY_ID, queueId), HttpMethods.GET)
                .body();
        return (String) new JSONObject(body).get("shortName");
    }


}
