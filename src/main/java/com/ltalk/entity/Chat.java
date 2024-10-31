package com.ltalk.entity;

import com.ltalk.request.ChatRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
@Setter
@Getter
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String receiver;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String message;

    @Column(name = "send_date", nullable = true)
    private LocalDateTime send_date;

    public Chat(ChatRequest chatRequest) {
        this.receiver = chatRequest.getReceiver();
        this.sender = chatRequest.getSender();
        this.message = chatRequest.getMessage();
        this.send_date = chatRequest.getSendDate();
    }
}
