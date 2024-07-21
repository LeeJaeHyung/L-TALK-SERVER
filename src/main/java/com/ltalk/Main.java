package com.ltalk;

import com.ltalk.controller.MainController;
import com.ltalk.controller.ServerSocketController;
import com.ltalk.entity.User;
import com.ltalk.entity.UserRole;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    public static ExecutorService threadPool;
    private static ServerSocket serverSocket;
    public static MainController control;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("[" + Thread.currentThread().getName() + "] on start()");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ServerMain.fxml"));
        Parent parent = loader.load();
        control = loader.getController();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LTalk-Sever");
        primaryStage.getIcons().add(new Image("file:src/images/icon.png"));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("종료 됨");
            stopServer(); // 서버 종료 처리
        });
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        stopServer(); // 서버 종료
        super.stop();
    }

    public static void startServer() {
        System.out.println("[" + Thread.currentThread().getName() + "]: StartServer() is Start");
        threadPool = Executors.newFixedThreadPool(10);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 7623));
        } catch (IOException e) {
            System.out.println("ServerSocket initialization error");
            e.printStackTrace();
            stopServer();
            return;
        }
        Runnable runnable = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "]: in running to accept()");
                    Socket socket = serverSocket.accept();
                    String text = "[연결수락: "+socket.getRemoteSocketAddress()+": "+Thread.currentThread().getName()+"]";
                    System.out.println(text);
                    User user = new User(socket.getInetAddress().getHostAddress(), socket.getInetAddress().getHostAddress(), UserRole.UNKNOWN_USER, null);
                    ServerSocketController.getSocketList().put(user.getIp(),new ServerSocketController(user, socket));
                    // 클라이언트와의 소켓 연결 처리
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        System.out.println("ServerSocket closed, terminating...");
                        break;
                    }
                    System.out.println("Error accepting connection");
                    e.printStackTrace();
                }
            }
        };
        threadPool.submit(runnable);
    }

    public static void stopServer() {
        System.out.println("[" + Thread.currentThread().getName() + "] : stopServer()");
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.err.println("Error closing ServerSocket");
                e.printStackTrace();
            }
        }
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdownNow(); // 스레드 강제 종료
        }
    }
}
