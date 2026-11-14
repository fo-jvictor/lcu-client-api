package com.jvictor01.matchmaking.dtos;

import lombok.Getter;

public class AutoAcceptSettings {
    @Getter
    private static volatile boolean enabled = false;

    public static void setEnabled(boolean enabled) {
        AutoAcceptSettings.enabled = enabled;
    }
}
