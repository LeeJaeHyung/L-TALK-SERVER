package com.ltalk.service;

import com.ltalk.controller.ServerSocketController;
import com.ltalk.entity.Chat;
import com.ltalk.entity.ServerResponse;
import com.ltalk.enums.ProtocolType;
import com.ltalk.repository.ChatRepository;
import com.ltalk.request.ChatRequest;
import com.ltalk.response.ChatResponse;

import java.io.IOException;

public class ChatService {
    ServerSocketController socketController;
    ChatRepository chatRepository;
    public ChatService(ServerSocketController socketController){
        this.socketController = socketController;
        chatRepository = new ChatRepository();
    }

    public void send(ChatRequest request) throws IOException {
        String receiver = request.getReceiver();
        ServerSocketController ssc = ServerSocketController.getSocketList().get(receiver);
        if(ssc != null){// 수신자가 접속중
            ssc.sendResponse(new ServerResponse(ProtocolType.CHAT, true, new ChatResponse(request)));
            //DB저장
            chatRepository.save(new Chat(request));
        }else{// 수신자가 비접속
            //DB저장
            chatRepository.save(new Chat(request));
        }
    }
}
