package com.jvictor01.lobby;

import java.util.Arrays;

public enum QueueIdEnum {
    //TODO: Add all queue ids
    ARAM(450),
    ARAM_DESORDEM(3270),
    RANKED_FLEX(440),
    RANKED_SOLO_DUO(420),
    ARENA(1700),
    NORMAL_BLIND(430),
    NORMAL_DRAFT(400),
    ONE_FOR_ALL(1020),
    ARURF(900);


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
