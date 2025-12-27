package com.jvictor01.controllers;

public class RequestRouteHelper {

    public static String getSubpathByBaseRoute(String baseRoute, String fullPath) {
        if (!fullPath.startsWith(baseRoute)) {
            return fullPath;
        }
        return fullPath.substring(baseRoute.length());
    }

}
