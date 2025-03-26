package com.jvictor01.matchmaking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;

public class MatchmakingService {
    private final HttpUtils httpUtils;;

    public MatchmakingService() {
        this.httpUtils = new HttpUtils();
    }

    public HttpResponse<String> postReadyCheckAccept() {
        System.out.println("CALLING READY CHECK SERVICE");
        return httpUtils.buildPostRequest(MatchmakingEndpoints.READY_CHECK_ACCEPT);
    }

    public HttpResponse<String> postReadyCheckDecline() {
        System.out.println("DECLINING MATCH");
        return httpUtils.buildPostRequest(MatchmakingEndpoints.READY_CHECK_DECLINE);
    }


}
