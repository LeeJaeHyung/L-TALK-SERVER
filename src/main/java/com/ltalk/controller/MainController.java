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
        MemberRepository memberRepository = new MemberRepository();
        Member member = memberRepository.findByUserName("a");
        Member member2 = memberRepository.findByUserName("as");
        Member member3 = memberRepository.findByUserName("asd");
        Set<Friend> friends = member.getFriends();
        for (Friend friend : friends) {
            System.out.println(friend.getFriend().getUsername());
        }
        friends.add(new Friend(member,member3,FriendStatus.ACCEPTED));
        Set<Friend> friends2 = member2.getFriends();
        for (Friend friend : friends2) {
            System.out.println(friend.getFriend().getUsername());
        }
        friends2.add(new Friend(member2,member3,FriendStatus.ACCEPTED));
//        memberRepository.update(member);
//        memberRepository.update(member2);
        Set<Friend> friends3 = member3.getFriends();
//        friends3.add(new Friend(member3,member,FriendStatus.ACCEPTED));
//        friends3.add(new Friend(member3,member2,FriendStatus.ACCEPTED));
//        memberRepository.update(member3);
        for (Friend friend : friends3) {
            System.out.println(friend.getFriend().getUsername());
        }

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
