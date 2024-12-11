package com.ltalk.dto;

import com.ltalk.entity.ChatRoomMember;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomDTO {
    List<String> members = new ArrayList<String>();
    String roomName;
    Long roomId;

    public ChatRoomDTO(ChatRoomMember chatRoomMember){
        chatRoomMember.getMember();
    }
}
