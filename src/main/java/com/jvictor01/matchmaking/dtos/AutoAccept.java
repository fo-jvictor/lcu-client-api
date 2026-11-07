package com.jvictor01.matchmaking.dtos;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class AutoAccept {
    private boolean enabled;

    public AutoAccept(boolean enabled) {
        this.enabled = enabled;
    }

    public AutoAccept(JSONObject json) {
        this.enabled = json.getBoolean("enabled");
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
