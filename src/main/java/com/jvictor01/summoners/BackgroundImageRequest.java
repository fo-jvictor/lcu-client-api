package com.jvictor01.summoners;

import org.json.JSONObject;

public class BackgroundImageRequest {
    private String key;
    private Long value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public BackgroundImageRequest(JSONObject jsonObject) {
        this.key = "backgroundSkinId";
        this.value = jsonObject.getLong("backgroundSkinId");
    }

}
