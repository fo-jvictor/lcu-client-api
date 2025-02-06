package com.jvictor01.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.lobby.Invitation;
import com.jvictor01.trust_manager.SSLContextFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.jvictor01.authentication.LeagueClientAuthentication.*;

public class HttpUtils {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpUtils() {
        this.httpClient = HttpClient.newBuilder()
                .sslContext(SSLContextFactory.createTrustAllSSLContext())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public HttpResponse<String> buildPostRequest(String path, String requestBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> buildPostRequest(String path, Object requestBody) {
        var requestBodyString = writeRequestBody(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyString, StandardCharsets.UTF_8))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T buildPostRequestBy(String path, Object body, Class<T> type) {
        String requestBody = writeRequestBody(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return readResponseBody(response, type);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> buildPostRequest(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> buildDeleteRequest(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> buildGetRequest(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T buildGetRequestBy(String path, Class<T> expectedResponse) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return readResponseBody(response, expectedResponse);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Invitation> getLobbyInvitations(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<List<Invitation>>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public HttpResponse<String> buildPutRequest(String path, Object requestBody) {
        var requestBodyString = writeRequestBody(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LCU_BASE_ENDPOINT + LCU_SERVER_PORT + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN)
                .header("Accept", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBodyString, StandardCharsets.UTF_8))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T readResponseBody(HttpResponse<String> response, Class<T> valueType) {
        try {
            return objectMapper.readValue(response.body(), valueType);
        } catch (JsonProcessingException e) {
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
