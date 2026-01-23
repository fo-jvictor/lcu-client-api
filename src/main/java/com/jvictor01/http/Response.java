package com.jvictor01.http;

public class Response<T> {
    private T body;
    private int statusCode;

    public Response(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}

