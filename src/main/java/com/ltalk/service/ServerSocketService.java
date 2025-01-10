package com.ltalk.service;

import com.ltalk.controller.ServerSocketController;
import com.ltalk.entity.Member;
import com.ltalk.entity.ServerResponse;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ServerSocketService {

    ChatService chatService;
    MemberService memberService;

    public ServerSocketService() {
        chatService = new ChatService();
        memberService = new MemberService();
    }


    public void chat(ChatRequest request) throws IOException {
        chatService.send(request);
    }
    public ServerResponse login(LoginRequest request, String ip) throws NoSuchAlgorithmException, IOException {
        Member member = new Member(request);
        ServerResponse response = null;
        try{
            response = memberService.login(member,ip);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
    private void logout(){

    }
    public ServerResponse signup(SignupRequest request) throws NoSuchAlgorithmException, IOException {
        Member member = new Member(request);
        return memberService.signup(member);
    }
}
