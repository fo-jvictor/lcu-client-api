package com.jvictor01.lobby.team_builder.dtos;

import org.json.JSONObject;

public class ChatDetails {
    private String multiUserChatId;
    private String multiUserChatPassword;
    private MucJwtDto mucJwtDto;

    public ChatDetails(JSONObject jsonObject) {
        this.multiUserChatId = jsonObject.getString("multiUserChatId");
        this.multiUserChatPassword = jsonObject.getString("multiUserChatPassword");
        this.mucJwtDto = new MucJwtDto(jsonObject.getJSONObject("mucJwtDto"));
    }

    public void setMultiUserChatId(String multiUserChatId) {
        this.multiUserChatId = multiUserChatId;
    }

    public void setMultiUserChatPassword(String multiUserChatPassword) {
        this.multiUserChatPassword = multiUserChatPassword;
    }

    public void setMucJwtDto(MucJwtDto mucJwtDto) {
        this.mucJwtDto = mucJwtDto;
    }

    public String getMultiUserChatId() {
        return multiUserChatId;
    }

    public String getMultiUserChatPassword() {
        return multiUserChatPassword;
    }

    public MucJwtDto getMucJwtDto() {
        return mucJwtDto;
    }
}
