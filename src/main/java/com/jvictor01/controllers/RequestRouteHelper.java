package com.jvictor01.controllers;

public class RequestRouteHelper {

    public static String getSubpathByBaseRoute(String baseRoute, String fullPath) {
        return fullPath.substring(baseRoute.length());
    }

}
