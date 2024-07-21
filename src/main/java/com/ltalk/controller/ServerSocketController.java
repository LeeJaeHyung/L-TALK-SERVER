package com.ltalk.controller;

import com.google.gson.Gson;
import com.ltalk.Main;
import com.ltalk.entity.Data;
import com.ltalk.entity.Member;
import com.ltalk.entity.User;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSocketController {

    private static final Map<String, ServerSocketController> SOCKET_LIST = new ConcurrentHashMap<>();
    private User user;
    private String ip;
    private Socket socket;
    private Gson gson = new Gson();

    public  ServerSocketController(User user, Socket socket) throws IOException {
        this.user = user;
        this.socket = socket;
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
    private void signup(SignupRequest request) throws NoSuchAlgorithmException {
        Member member = new Member(request);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ltalk");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(member);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
