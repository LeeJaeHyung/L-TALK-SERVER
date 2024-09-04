package com.ltalk.service;

import com.ltalk.controller.ServerSocketController;
import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import com.ltalk.repository.FriendRepository;

import java.util.List;

public class FriendService {
    ServerSocketController serverSocketController;
    FriendRepository friendRepository;

    public FriendService(ServerSocketController serverSocketController) {
        this.serverSocketController = serverSocketController;
        friendRepository = new FriendRepository();
    }

    public List<Friend> getFriendList(Member member) {
//        friendRepository.friendRequest(member, "b");
        List<Friend> friendList = friendRepository.getFriendList(member.getUsername());
        for (Friend friend : friendList) {
            System.out.println(friend.getFriend_name());
        }

        return friendList;
    }
}
