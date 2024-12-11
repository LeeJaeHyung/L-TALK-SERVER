package com.ltalk.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_room_members")
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 'chatRoom' 필드 타입은 'ChatRoom'이어야 하며, @ManyToOne 관계가 올바르게 설정되어야 합니다.
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    // 'member' 필드 타입도 'Member'로 설정
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime joinedAt;
}

