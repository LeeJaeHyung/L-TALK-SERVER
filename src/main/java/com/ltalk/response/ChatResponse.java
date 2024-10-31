package com.ltalk.response;

import com.ltalk.request.ChatRequest;
import lombok.Getter;

@Getter
public class ChatResponse {
    private String receiver;
    private String sender;
    private String message;
    public ChatResponse(ChatRequest chatRequest) {
        this.receiver = chatRequest.getReceiver();
        this.sender = chatRequest.getSender();
        this.message = chatRequest.getMessage();
    }
}
