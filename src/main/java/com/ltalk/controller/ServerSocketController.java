package com.ltalk.controller;

import com.google.gson.Gson;
import com.ltalk.Main;
import com.ltalk.entity.*;
import com.ltalk.repository.MemberRepository;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import com.ltalk.response.SignupResponse;
import com.ltalk.service.MemberService;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSocketController {

    private static final Map<String, ServerSocketController> SOCKET_LIST = new ConcurrentHashMap<>();
    @Getter
    private User user;
    private String ip;
    private OutputStream outputStream;
    private Socket socket;
    private Gson gson = new Gson();
    private final MemberService memberService = new MemberService(this);

    public  ServerSocketController(User user, Socket socket) throws IOException {
        this.user = user;
        this.socket = socket;
        this.outputStream = socket.getOutputStream();
        start();
    }

    public static void readMode(byte modeByte){
        System.out.println(modeByte);
    }

    public static Map<String,ServerSocketController> getSocketList(){
        return SOCKET_LIST;
    }

    public void start()throws IOException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        InputStream is = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bufferLength = is.read(buffer);
                        if(bufferLength == -1) {
                            System.out.println("["+Thread.currentThread().getName()+"] <= data read Error");
                        }else{
                            String dataString = new String(buffer, 0, bufferLength, "UTF-8");
                            System.out.println("Data => " + dataString);
                            Data data = gson.fromJson(dataString, Data.class);
                            switch (data.getProtocolType()) {
                                case CHAT -> chat(data.getChatRequest());
                                case LOGIN -> login(data.getLoginRequest());
                                case SIGNUP -> signup(data.getSignupRequest());
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        try {
                            socket.getInputStream().close();
                            socket.getOutputStream().close();
                            socket.close();
                            System.out.println(user.getUsername()+"님의 소켓이 닫힘");
                            break;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        };
        Main.threadPool.submit(runnable);
    }

    private void chat(ChatRequest request){

    }
    private void login(LoginRequest request) throws NoSuchAlgorithmException, IOException {
        Member member = new Member(request);
        memberService.login(member);
    }
    private void logout(){

    }
    private void sendMsg(String msg){

    }
    private void signup(SignupRequest request) throws NoSuchAlgorithmException, IOException {
        Member member = new Member(request);
        memberService.signup(member);
    }

    public void sendResponse(ServerResponse response) throws IOException {
        String dataString = gson.toJson(response);
        byte[] buffer = dataString.getBytes();
        outputStream.write(buffer);
        outputStream.flush();
        System.out.println("Response 전송 완료!");
    }

}
