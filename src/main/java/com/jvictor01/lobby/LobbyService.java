package com.jvictor01.lobby;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.http.Response;
import com.jvictor01.lobby.dtos.*;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.summoners.dtos.Summoner;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.*;

public class LobbyService {
    private final HttpWebClient httpWebClient;
    private final SummonerService summonerService;
    private final ObjectMapper objectMapper;

    public LobbyService() {
        this.httpWebClient = new HttpWebClient();
        this.summonerService = new SummonerService();
        this.objectMapper = new ObjectMapper();
    }

    public HttpResponse<String> matchmakingSearch() {
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH, HttpMethods.POST);
    }

    public Response<SearchState> getLobbySearchState() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(LobbyEndpoints.SEARCH_STATE, HttpMethods.GET);
        try {
            SearchState searchState = objectMapper.readValue(response.body(), SearchState.class);
            return new Response<>(response.statusCode(), searchState);
        } catch (JsonProcessingException e) {
            return new Response<>(500, null);
        }
    }

    public HttpResponse<String> cancelMatchmakingSearch() {
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH, HttpMethods.DELETE);
    }

    public HttpResponse<String> createLobby(LobbySettings lobbySettings) {
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2, HttpMethods.POST, lobbySettings);
    }

    public Response<GameQueue> getCurrentLobby() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(LobbyEndpoints.LOBBY_V2, HttpMethods.GET);
        JSONObject gameConfig = new JSONObject(response.body()).getJSONObject("gameConfig");
        Integer queueId = (Integer) gameConfig.get("queueId");

        HttpResponse<String> currentLobby = httpWebClient.buildRequestForLcu(String.format(LobbyEndpoints.GAME_QUEUE_BY_ID, queueId), HttpMethods.GET);

        try {
            GameQueue gameQueue = objectMapper.readValue(currentLobby.body(), new TypeReference<>() {
            });

            String friendlyLobbyName = Optional.ofNullable(QueueIdEnum.getFromId(queueId))
                    .map(QueueIdEnum::getFriendlyQueueName)
                    .orElse(gameQueue.getShortName());

            gameQueue.setShortName(friendlyLobbyName);

            return new Response<>(response.statusCode(), gameQueue);

        } catch (Exception e) {
            return new Response<>(500, null);
        }
    }

    public Response<List<SuggestedPlayer>> getSuggestedPlayers() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(LobbyEndpoints.SUGGESTED_PLAYERS, HttpMethods.GET);

        try {
            List<SuggestedPlayer> suggestedPlayers = objectMapper.readValue(response.body(), new TypeReference<>() {
            });

            List<SuggestedPlayer> onlineFriend = suggestedPlayers.stream()
                    .filter(suggestedPlayer -> suggestedPlayer.getReason().equalsIgnoreCase("OnlineFriend"))
                    .filter(suggestedPlayer -> suggestedPlayer.getSummonerName().isEmpty() || suggestedPlayer.getSummonerName() == null)
                    .map(suggestedPlayer -> {
                        Summoner summonerById = summonerService.getSummonerById(suggestedPlayer.getSummonerId());
                        return new SuggestedPlayer(suggestedPlayer.getReason(), suggestedPlayer.getSummonerId(), getSummonerName(summonerById));
                    })
                    .toList();

            return new Response<>(response.statusCode(), onlineFriend);

        } catch (Exception e) {
            return new Response<>(500, null);
        }

    }

    private String getSummonerName(Summoner summoner) {
        boolean isSummonerDisplayNameEmpty = summoner.getDisplayName().isEmpty() || summoner.getDisplayName() == null;
        boolean isSummonerGameNameEmpty = summoner.getGameName().isEmpty() || summoner.getGameName() == null;
        boolean isSummonerInternalNameEmpty = summoner.getInternalName().isEmpty() || summoner.getInternalName() == null;

        if (!isSummonerDisplayNameEmpty) return summoner.getDisplayName();
        if (!isSummonerGameNameEmpty) return summoner.getGameName();
        if (!isSummonerInternalNameEmpty) return summoner.getInternalName();

        return "";
    }

    public HttpResponse<String> updatePositionPreferences(LobbyRoles lobbyRoles) {
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.POSITION_PREFERENCES, HttpMethods.PUT, lobbyRoles);
    }

    public HttpResponse<String> kickSummonerBySummonerId(String summonerId) {
        String formattedUri = String.format(LobbyEndpoints.KICK_SUMMONER_BY_SUMMONER_ID, summonerId);
        return httpWebClient.buildRequestForLcu(formattedUri, HttpMethods.POST);
    }

    public Response<LobbyRoles> getPlayerSlots() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(LobbyEndpoints.PLAYER_SLOTS, HttpMethods.GET);
        try {
            List<PlayerSlots> playerSlots = objectMapper.readValue(response.body(), new TypeReference<>() {
            });
            LobbyRoles lobbyRoles = new LobbyRoles();

            PlayerSlots playerSlots1 = playerSlots.get(0);
            lobbyRoles.setFirstPreference(playerSlots1.getPositionPreference());

            PlayerSlots playerSlots2 = playerSlots.get(1);
            lobbyRoles.setSecondPreference(playerSlots2.getPositionPreference());

            return new Response<>(response.statusCode(), lobbyRoles);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<List<GameQueue>> getAvailableGameQueues() {

        HttpResponse<String> response =
                httpWebClient.buildRequestForLcu(LobbyEndpoints.GAME_QUEUES, HttpMethods.GET);

        try {
            List<GameQueue> queues = objectMapper.readValue(response.body(), new TypeReference<>() {
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
        Summoner[] summoners = summonerService.getSummonerDetailsByNickname(nickname + "#" + tag);

        Invitation invitation = new Invitation();
        invitation.setToSummonerId(summoners[0].getSummonerId());
        invitation.setToSummonerName(summoners[0].getGameName());

        return httpWebClient.buildRequestForLcu(LobbyEndpoints.INVITATIONS_V2, HttpMethods.POST, Collections.singletonList(invitation));
    }

    public HttpResponse<String> postInvitationBySummonerId(String summonerId) {
        Invitation invitation = new Invitation();
        invitation.setToSummonerId(Long.parseLong(summonerId));
        return httpWebClient.buildRequestForLcu(LobbyEndpoints.INVITATIONS_V2, HttpMethods.POST, Collections.singletonList(invitation));
    }

    public Response<List<Invitation>> getInvitations() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(LobbyEndpoints.INVITATIONS_V2, HttpMethods.GET);
        Summoner myself = summonerService.loadOwnSummonerDetails();
        ArrayList<Invitation> filteredInvitations = new ArrayList<>();

        try {
            List<Invitation> invitations = objectMapper.readValue(response.body(), new TypeReference<>() {
            });

            invitations.stream()
                    .filter(invitation -> !Objects.equals(myself.getPuuid(), invitation.getToPuuid()))
                    .peek(invitation -> {
                        Summoner summonerByPuuid = summonerService.getSummonerByPuuid(invitation.getToPuuid());
                        invitation.setToSummonerName(getSummonerName(summonerByPuuid));
                    })
                    .forEach(filteredInvitations::add);

            return new Response<>(response.statusCode(), filteredInvitations);
        } catch (JsonProcessingException e) {
            return new Response<>(response.statusCode(), null);
        }
    }


}
