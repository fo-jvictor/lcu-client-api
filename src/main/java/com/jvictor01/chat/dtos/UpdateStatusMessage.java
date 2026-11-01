package com.jvictor01.chat.dtos;

import org.json.JSONObject;

public class UpdateStatusMessage {
    private String statusMessage;

    public UpdateStatusMessage(JSONObject json) {
        this.statusMessage = json.optString("statusMessage", "");
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
