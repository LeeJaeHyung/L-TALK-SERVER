package com.ltalk.controller;

import com.google.gson.Gson;
import com.ltalk.Main;
import com.ltalk.entity.Data;
import com.ltalk.entity.User;
import lombok.Getter;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSocketController {

    private static final Map<String, ServerSocketController> SOCKET_LIST = new ConcurrentHashMap<>();
    private User user;
    private Socket socket;

    public  ServerSocketController(User user, Socket socket){
        this.user = user;
        this.socket = socket;
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
                            Gson gson = new Gson();
                            Data data = gson.fromJson(dataString, Data.class);
                            switch (data.getProtocolType()) {
                                case CHAT -> chat();
                                case LOGIN -> login();
                                case SIGNUP -> signup();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Main.threadPool.submit(runnable);
    }

    private void chat(){

    }
    private void login(){

    }
    private void logout(){

    }
    private void sendMsg(String msg){

    }
    private void signup(){

    }
}
