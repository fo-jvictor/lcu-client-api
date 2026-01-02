package com.jvictor01.riot_client;

import com.jvictor01.utils.HttpUtils;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public class RiotSignOnService {
    private final HttpUtils httpUtils = new HttpUtils();

    public String getAccessToken() {
        HttpResponse<String> response = httpUtils.buildGetRequestForRiotClient(RiotSignOnEndpoints.ACCESS_TOKEN);
        JSONObject jsonObject = new JSONObject(response.body());
        return (String) jsonObject.get("token");
    }
}
