package com.jvictor01.matchmaking;

import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;

public class MatchmakingService {
    private final HttpUtils httpUtils = new HttpUtils();

    public HttpResponse<String> postReadyCheckAccept() {
        return httpUtils.buildPostRequest(MatchmakingEndpoints.READY_CHECK_ACCEPT);
    }

    public HttpResponse<String> postReadyCheckDecline() {
        return httpUtils.buildPostRequest(MatchmakingEndpoints.READY_CHECK_DECLINE);
    }


}
