package com.jvictor01.matchmaking;

import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;

import java.net.http.HttpResponse;

public class MatchmakingService {
    private final HttpWebClient httpWebClient = new HttpWebClient();

    public HttpResponse<String> postReadyCheckAccept() {
        return httpWebClient.buildRequestForLcu(MatchmakingEndpoints.READY_CHECK_ACCEPT, HttpMethods.POST);
    }

    public HttpResponse<String> postReadyCheckDecline() {
        return httpWebClient.buildRequestForLcu(MatchmakingEndpoints.READY_CHECK_DECLINE, HttpMethods.POST);
    }


}
