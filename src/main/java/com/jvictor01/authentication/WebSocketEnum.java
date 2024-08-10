package com.jvictor01.authentication;

public enum WebSocketEnum {
    SUBSCRIBE(5),
    UNSUBSCRIBE(6),
    PUBLISH(7),
    EVENT(8);

    private final int id;

    WebSocketEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
