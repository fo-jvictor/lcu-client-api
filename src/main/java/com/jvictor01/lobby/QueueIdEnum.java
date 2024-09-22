package com.jvictor01.lobby;

import java.util.Arrays;

public enum QueueIdEnum {
    ARAM(450),
    RANKED_FLEX(440),
    RANKED_SOLO_DUO(420);

    private final int id;

    QueueIdEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static QueueIdEnum getFromId(int queueId) {
        return Arrays.stream(QueueIdEnum.values())
                .filter(queueIdEnum -> queueIdEnum.getId() == queueId)
                .findFirst()
                .orElse(null);
    }

}
