package com.ltalk.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRequest {
    private String receiver;
    private String sender;
    private String message;
    private LocalDateTime sendDate;
}
