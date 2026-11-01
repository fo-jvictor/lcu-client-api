package com.jvictor01.chat.dtos;

import org.json.JSONObject;

public class UpdateChatIcon {
    private Long icon;

    public UpdateChatIcon(JSONObject json) {
        this.icon = json.getLong("profileIconId");
    }

    public Long getIcon() {
        return icon;
    }

    public void setIcon(Long icon) {
        this.icon = icon;
    }
}
