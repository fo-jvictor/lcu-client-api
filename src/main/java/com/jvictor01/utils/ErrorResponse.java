package com.jvictor01.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private String errorCode;
    private int httpStatus;
    private Map<String, Object> implementationDetails;
    private String message;

    public ErrorResponse() {}

    public ErrorResponse(JSONObject jsonObject) {
        this.errorCode = jsonObject.getString("errorCode");
        this.httpStatus = jsonObject.getInt("httpStatus");
        JSONObject details = jsonObject.getJSONObject("implementationDetails");

        this.implementationDetails = new HashMap<>();
        for (String key : details.keySet()) {
            this.implementationDetails.put(key, details.get(key));
        }

        this.message = jsonObject.getString("message");;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Map<String, Object> getImplementationDetails() {
        return implementationDetails;
    }

    public void setImplementationDetails(Map<String, Object> implementationDetails) {
        this.implementationDetails = implementationDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorCode='" + errorCode + '\'' +
                ", httpStatus=" + httpStatus +
                ", implementationDetails=" + implementationDetails +
                ", message='" + message + '\'' +
                '}';
    }
}
