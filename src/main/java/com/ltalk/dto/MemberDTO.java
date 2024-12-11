package com.ltalk.dto;

import com.ltalk.entity.ChatRoomMember;
import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;

import java.util.HashSet;
import java.util.Set;

public class MemberDTO {

    Long id;
    String username;
    String email;
    Set<ChatRoomDTO> chatRooms;
    Set<String> friends;

    public MemberDTO(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.chatRooms = new HashSet<>();
        this.friends = new HashSet<>();
    }

    public void transfer(Member member){
        //구현 필요
    }
}
