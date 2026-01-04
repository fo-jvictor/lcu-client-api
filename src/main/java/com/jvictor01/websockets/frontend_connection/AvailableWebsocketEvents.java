package com.jvictor01.websockets.frontend_connection;

public enum AvailableWebsocketEvents {
    SEARCH_STATE_QUEUE_STARTED("QUEUE STARTED"),
    SEARCH_STATE_QUEUE_ENDED("QUEUE ENDED"),
    SEARCH_STATE_MATCH_FOUND("MATCH FOUND");

    private String value;

    AvailableWebsocketEvents(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
