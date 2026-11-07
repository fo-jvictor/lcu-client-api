package com.jvictor01.chat;

import com.jvictor01.chat.dtos.BotFriendRequest;
import com.jvictor01.chat.dtos.BotReason;
import com.jvictor01.chat.dtos.DeclineBotsResult;
import com.jvictor01.chat.dtos.FriendRequest;
import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.http.Response;
import com.jvictor01.summoners.SummonerEndpoints;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestService {
    public static final long BOT_MAX_SUMMONER_LEVEL = 5;

    private final HttpWebClient httpWebClient;

    public FriendRequestService() {
        this.httpWebClient = new HttpWebClient();
    }

    public Response<List<FriendRequest>> getFriendRequests() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(ChatEndpoints.FRIEND_REQUESTS, HttpMethods.GET);
        if (!isSuccess(response.statusCode())) {
            return new Response<>(response.statusCode(), List.of());
        }

        JSONArray json = new JSONArray(response.body());
        List<FriendRequest> friendRequests = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            friendRequests.add(new FriendRequest(json.getJSONObject(i)));
        }
        return new Response<>(response.statusCode(), friendRequests);
    }

    public Response<List<BotFriendRequest>> findBotFriendRequests() {
        Response<List<FriendRequest>> friendRequests = getFriendRequests();
        if (!isSuccess(friendRequests.getStatusCode())) {
            return new Response<>(friendRequests.getStatusCode(), List.of());
        }

        List<BotFriendRequest> bots = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests.getBody()) {
            if (!friendRequest.isIncoming()) {
                continue;
            }

            BotFriendRequest bot = analyze(friendRequest);
            if (bot != null) {
                bots.add(bot);
            }
        }
        return new Response<>(200, bots);
    }

    public Response<DeclineBotsResult> declineBotFriendRequests() {
        Response<List<BotFriendRequest>> bots = findBotFriendRequests();
        if (!isSuccess(bots.getStatusCode())) {
            return new Response<>(bots.getStatusCode(), new DeclineBotsResult());
        }

        DeclineBotsResult result = new DeclineBotsResult();
        for (BotFriendRequest bot : bots.getBody()) {
            HttpResponse<String> response = httpWebClient.buildRequestForLcu(
                    ChatEndpoints.FRIEND_REQUEST_BY_PUUID + bot.getPuuid(), HttpMethods.DELETE);

            if (isSuccess(response.statusCode())) {
                result.addDeclined(bot);
            } else {
                result.addFailed(bot);
            }
        }
        return new Response<>(200, result);
    }

    private BotFriendRequest analyze(FriendRequest friendRequest) {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(
                SummonerEndpoints.SUMMONER_BY_PUUID + friendRequest.getPuuid(), HttpMethods.GET);

        if (response.statusCode() == 404) {
            return new BotFriendRequest(friendRequest, null, BotReason.NO_LOL_ACCOUNT);
        }

        if (!isSuccess(response.statusCode())) {
            return null;
        }

        long summonerLevel = new JSONObject(response.body()).optLong("summonerLevel", Long.MAX_VALUE);
        if (summonerLevel <= BOT_MAX_SUMMONER_LEVEL) {
            return new BotFriendRequest(friendRequest, summonerLevel, BotReason.LOW_LEVEL);
        }
        return null;
    }

    private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
}
