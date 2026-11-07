package com.jvictor01.chat.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeclineBotsResult {
    private final List<BotFriendRequest> declined = new ArrayList<>();
    private final List<BotFriendRequest> failed = new ArrayList<>();

    public void addDeclined(BotFriendRequest botFriendRequest) {
        declined.add(botFriendRequest);
    }

    public void addFailed(BotFriendRequest botFriendRequest) {
        failed.add(botFriendRequest);
    }
}
