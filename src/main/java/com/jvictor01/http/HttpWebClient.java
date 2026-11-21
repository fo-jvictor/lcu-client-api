package com.jvictor01.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.utils.trust_manager.SSLContextFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.jvictor01.app_setup.LeagueApisSetup.*;

public class HttpWebClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpWebClient() {
        this.httpClient = HttpClient.newBuilder()
                .sslContext(SSLContextFactory.createTrustAllSSLContext())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public HttpResponse<String> buildRequestForLcu(String endpoint, HttpMethods method, Object requestBody) {
        return buildAndExecuteRequestBy(endpoint, AvailableApis.LCU, method, requestBody);
    }

    public HttpResponse<String> buildRequestForLcu(String endpoint, HttpMethods method) {
        return buildAndExecuteRequestBy(endpoint, AvailableApis.LCU, method, null);
    }

    public HttpResponse<String> buildRequestForRiotClientApi(String endpoint, HttpMethods method) {
        return buildAndExecuteRequestBy(endpoint, AvailableApis.RIOT_CLIENT, method, null);
    }

    private HttpResponse<String> buildAndExecuteRequestBy(String endpoint, AvailableApis api, HttpMethods method, Object requestBody) {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        if (api == null || Objects.equals(api.getValue(), AvailableApis.LCU.getValue())) {
            URI uri = URI.create(LOCAL_HOST + LCU_SERVER_PORT + endpoint);
            httpRequestBuilder.uri(uri);
            httpRequestBuilder.setHeader("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN);
        } else if (Objects.equals(api.getValue(), AvailableApis.RIOT_CLIENT.getValue())) {
            URI uri = URI.create(LOCAL_HOST + RIOT_CLIENT_SERVER_PORT + endpoint);
            httpRequestBuilder.uri(uri);
            httpRequestBuilder.setHeader("Authorization", "Basic " + RIOT_CLIENT_AUTH_TOKEN);
        }

        if (requestBody == null) {
            httpRequestBuilder.method(method.getValue(), HttpRequest.BodyPublishers.noBody());
        } else {
            httpRequestBuilder.method(method.getValue(),
                    HttpRequest.BodyPublishers.ofString(writeRequestBody(requestBody), StandardCharsets.UTF_8));
        }

        try {
            return this.httpClient.send(httpRequestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private String writeRequestBody(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
