package com.jvictor01.matchmaking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

public class MatchmakingService {
    private final HttpUtils httpUtils;;

    public MatchmakingService() {
        this.httpUtils = new HttpUtils();
    }

    public void postReadyCheckAccept() {
        System.out.println("CALLING READY CHECK SERVICE? Thread: " + Thread.currentThread().getName());
        httpUtils.buildPostRequest(MatchmakingEndpoints.READY_CHECK_ACCEPT);
    }

    public void postReadyCheckDecline() {
        System.out.println("DECLINING MATCH");
        httpUtils.buildPostRequest(MatchmakingEndpoints.READY_CHECK_DECLINE);
    }

//    public Object getMatchmakingSearch() {
//        httpUtils.buildGetRequest(MatchmakingEndpoints.MATCHMAKING_SEARCH);
//        return
//    }

}
