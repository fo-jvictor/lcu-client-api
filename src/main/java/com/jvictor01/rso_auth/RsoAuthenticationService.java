package com.jvictor01.rso_auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;

public class RsoAuthenticationService {
    private final HttpUtils httpUtils;
    private final ObjectMapper objectMapper;

    public RsoAuthenticationService() {
        this.httpUtils = new HttpUtils();
        this.objectMapper = new ObjectMapper();
    }

    public Authorization getAuthorization() {
        HttpResponse<String> response = httpUtils.buildGetRequest("/lol-rso-auth/v1/authorization");
        try {
            return objectMapper.readValue(response.body(), Authorization.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Authorization postAuthorizationGas(String username, String password) {
        String requestBody = null;

        AuthorizationGas authorizationGas = new AuthorizationGas();
        authorizationGas.setUsername(username);
        authorizationGas.setPassword(password);
        authorizationGas.setPlatformId(getAuthorization().getCurrentPlatformId());
        try {
            requestBody = objectMapper.writeValueAsString(authorizationGas);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = httpUtils.buildPostRequest("/lol-rso-auth/v1/authorization/gas", requestBody);
        try {
            return objectMapper.readValue(response.body(), Authorization.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
