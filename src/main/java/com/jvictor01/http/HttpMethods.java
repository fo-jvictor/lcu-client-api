package com.jvictor01.http;

public enum HttpMethods {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS");

    private String value;

    HttpMethods(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
