package com.ltalk.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Set;

import com.ltalk.entity.*;
import com.ltalk.enums.FriendStatus;
import com.ltalk.repository.ChatRoomMemberRepository;
import com.ltalk.repository.ChatRoomRepository;
import com.ltalk.repository.FriendRepository;
import com.ltalk.repository.MemberRepository;
import com.ltalk.util.JpaUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import com.ltalk.Main;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContexts;
import javax.persistence.PrePersist;

public class MainController implements Initializable{

    @FXML
    TextArea textArea;
    @FXML
    Button button;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)  {
    }

    public void buttonClick() {
        if(button.getText().equals("Start")) {
            try {
                Main.startServer();
                button.setText("Stop");
                textArea.appendText("[Server]-> Start\n");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                Main.stopServer();
                button.setText("Start");
                textArea.appendText("[Server]-> Stop\n");
            }catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void addText(String text) {
        Platform.runLater(()->{
            textArea.appendText(text+"\n");
        });
    }

}
