package com.jvictor01.riot_client;

import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public class RiotSignOnService {
    private final HttpWebClient httpWebClient = new HttpWebClient();

    public String getAccessToken() {
        HttpResponse<String> response = httpWebClient.buildRequestForRiotClientApi(RiotSignOnEndpoints.ACCESS_TOKEN, HttpMethods.GET);
        JSONObject jsonObject = new JSONObject(response.body());
        return (String) jsonObject.get("token");
    }
}
