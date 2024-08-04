package com.ltalk.controller;

import com.google.gson.Gson;
import com.ltalk.Main;
import com.ltalk.entity.*;
import com.ltalk.repository.MemberRepository;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import com.ltalk.response.SignupResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSocketController {

    private static final Map<String, ServerSocketController> SOCKET_LIST = new ConcurrentHashMap<>();
    private User user;
    private String ip;
    private OutputStream outputStream;
    private Socket socket;
    private Gson gson = new Gson();
    private final MemberRepository memberRepository = new MemberRepository();

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
    private void login(LoginRequest request){

    }
    private void logout(){

    }
    private void sendMsg(String msg){

    }
    private void signup(SignupRequest request) throws NoSuchAlgorithmException, IOException {
        Member member = new Member(request);
        if(duplication(member)){
            if(memberRepository.save(member)){
                sendResponse(new ServerResponse(ProtocolType.SIGNUP,true, new SignupResponse("회원가입 성공")));
            }else{
                sendResponse(new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("회원가입 실패")));
            }
        }
    }

    private boolean duplication(Member member) throws IOException {
        boolean nameCheck = memberRepository.usernameExists(member.getUsername());
        System.out.println("아이디 존재? "+nameCheck);
        boolean emailCheck = false;
        if(nameCheck==false){
            emailCheck = memberRepository.emailExists(member.getEmail());
            System.out.println("이메일 존재? "+emailCheck);
            if(emailCheck){
                sendResponse(new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 이메일이 이미 존재합니다.")));
            }
        }else{
            sendResponse(new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 아이디가 이미 존재합니다.")));
        }
        return !nameCheck && !emailCheck;
    }

    private void sendResponse(ServerResponse response) throws IOException {
        String dataString = gson.toJson(response);
        byte[] buffer = dataString.getBytes();
        outputStream.write(buffer);
        outputStream.flush();
    }
}
