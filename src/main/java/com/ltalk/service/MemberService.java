package com.ltalk.service;

import com.ltalk.controller.ServerSocketController;
import com.ltalk.entity.Member;
import com.ltalk.entity.ProtocolType;
import com.ltalk.entity.ServerResponse;
import com.ltalk.entity.User;
import com.ltalk.repository.MemberRepository;
import com.ltalk.response.LoginResponse;
import com.ltalk.response.SignupResponse;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MemberService {
    MemberRepository memberRepository;
    ServerSocketController socketController;

    public MemberService(ServerSocketController socketController) {
        memberRepository = new MemberRepository();
        this.socketController = socketController;
    }

    public void signup(Member member) throws NoSuchAlgorithmException, IOException {
        if(duplication(member)){
            if(memberRepository.save(member)){
                socketController.sendResponse(new ServerResponse(ProtocolType.SIGNUP,true, new SignupResponse("회원가입 성공")));
            }else{
                socketController.sendResponse(new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("회원가입 실패")));
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
                socketController.sendResponse(new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 이메일이 이미 존재합니다.")));
            }
        }else{
            socketController.sendResponse(new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 아이디가 이미 존재합니다.")));
        }
        return !nameCheck && !emailCheck;
    }


    public void login(Member member) throws IOException {
        if(memberRepository.usernameExists(member.getUsername())){
            System.out.println("멤버 존재");
            Member targetMember = memberRepository.findByUserName(member.getUsername());
            System.out.println("타겟 멤버 가져옴");
            if(targetMember.getPassword().equals(member.getPassword())){
                System.out.println("비밀번호 일치");
                User user = socketController.getUser();
                user.login(targetMember);
                socketController.sendResponse(new ServerResponse(ProtocolType.LOGIN, true, new LoginResponse("로그인 성공")));
                System.out.println("로그인 성공 전송");
            }else{
                System.out.println("비밀 번호 불일치");
                socketController.sendResponse(new ServerResponse(ProtocolType.LOGIN, false, new LoginResponse("비밀번호를 확인해 주세요")));
            }
        }else{
            System.out.println("해당 멤버 없음");
            socketController.sendResponse(new ServerResponse(ProtocolType.LOGIN, false, new LoginResponse("아이디를 확인해 주세요")));
        }
    }
}
