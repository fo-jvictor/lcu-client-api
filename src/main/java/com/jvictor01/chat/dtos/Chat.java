package com.jvictor01.chat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Chat {
    private String availability;
    private String gameName;
    private String gameTag;
    private int icon;
    private String id;
    private String statusMessage;
    private Long summonerId;
    private String puuid;
    private Lol lol;

    public Chat() {}
}
