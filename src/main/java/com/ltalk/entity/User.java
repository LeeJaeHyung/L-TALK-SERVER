package com.ltalk.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    private String ip;
    private String username;
    private UserRole userRoleEnum;
    private Member member;

    public void login(Member member){
        username = member.getUsername();
        userRoleEnum = UserRole.USER;
        this.member = member;
    }

}
