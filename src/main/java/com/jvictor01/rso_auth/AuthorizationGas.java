package com.jvictor01.rso_auth;

import org.json.JSONObject;

public class AuthorizationGas {
    private String password;
    private String platformId;
    private String username;

    public AuthorizationGas(JSONObject jsonObject) {
        this.password = jsonObject.getString("password");
        this.platformId = jsonObject.getString("platformId");
        this.username = jsonObject.getString("username");
    }

    public AuthorizationGas() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "AuthorizationGas{" +
                "password='" + password + '\'' +
                ", platformId='" + platformId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
