package com.jvictor01.lobby;

import java.util.Arrays;

public enum QueueIdEnum {
    //TODO: Add all queue ids
    ARAM(450, "Aram"),
    ARAM_DESORDEM(3270, "Aram Desordem"),
    RANKED_FLEX(440, "Ranked Flex"),
    RANKED_SOLO_DUO(420, "Ranked Solo Duo"),
    ARENA(1700, "Arena"),
    NORMAL_BLIND(430, "Normal Game às Cegas"),
    NORMAL_DRAFT(400, "Normal Game Alternada"),
    ONE_FOR_ALL(1020, "Todos Por Um"),
    ARURF(900, "ARURF");

    private final int id;
    private final String friendlyQueueName;

    QueueIdEnum(int id, String friendlyQueueName) {
        this.id = id;
        this.friendlyQueueName = friendlyQueueName;
    }

    public int getId() {
        return this.id;
    }

    public String getFriendlyQueueName() {
        return this.friendlyQueueName;
    }

    public static QueueIdEnum getFromId(int queueId) {
        return Arrays.stream(QueueIdEnum.values())
                .filter(queueIdEnum -> queueIdEnum.getId() == queueId)
                .findFirst()
                .orElse(null);
    }

}
