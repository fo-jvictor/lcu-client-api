package com.jvictor01.rso_auth;

import org.json.JSONObject;

public class Authorization {
    private int currentAccountId;
    private String currentPlatformId;
    private String subject;
    private String errorCode;

    public Authorization(JSONObject jsonObject) {
        this.currentAccountId = jsonObject.getInt("currentAccountId");
        this.currentPlatformId = jsonObject.getString("currentPlatformId");
        this.subject = jsonObject.getString("subject");
        this.errorCode = jsonObject.getString("errorCode");
    }

    public Authorization() {
    }

    public int getCurrentAccountId() {
        return currentAccountId;
    }

    public String getCurrentPlatformId() {
        return currentPlatformId;
    }

    public String getSubject() {
        return subject;
    }

    public void setCurrentAccountId(int currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    public void setCurrentPlatformId(String currentPlatformId) {
        this.currentPlatformId = currentPlatformId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "currentAccountId=" + currentAccountId +
                ", currentPlatformId='" + currentPlatformId + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
