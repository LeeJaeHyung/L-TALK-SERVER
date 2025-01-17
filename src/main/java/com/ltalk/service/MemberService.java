package com.ltalk.service;

import com.ltalk.controller.ServerSocketController;
import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import com.ltalk.entity.ServerResponse;
import com.ltalk.entity.User;
import com.ltalk.enums.ProtocolType;
import com.ltalk.repository.MemberRepository;
import com.ltalk.response.LoginResponse;
import com.ltalk.response.SignupResponse;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public class MemberService {
    MemberRepository memberRepository;

    public MemberService() {
        memberRepository = new MemberRepository();
    }

    public ServerResponse signup(Member member) throws NoSuchAlgorithmException, IOException {
        ServerResponse serverResponse = null;
        serverResponse = duplication(member);
        if(serverResponse == null){
            if(memberRepository.save(member)){
                return new ServerResponse(ProtocolType.SIGNUP,true, new SignupResponse("회원가입 성공"));
            }else{
                return new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("회원가입 실패"));
            }
        }
        return serverResponse;
    }

    private ServerResponse duplication(Member member) throws IOException {
        boolean nameCheck = memberRepository.usernameExists(member.getUsername());
        System.out.println("아이디 존재? "+nameCheck);
        boolean emailCheck = false;
        if(nameCheck==false){
            emailCheck = memberRepository.emailExists(member.getEmail());
            System.out.println("이메일 존재? "+emailCheck);
            if(emailCheck){
                return new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 이메일이 이미 존재합니다."));
            }
        }else{
            return new ServerResponse(ProtocolType.SIGNUP, false, new SignupResponse("중복된 아이디가 이미 존재합니다."));
        }
        return null;
    }


    public ServerResponse login(Member member, String ip) throws IOException {
        System.out.println("로그인 요청 IP: "+ip);
        if(memberRepository.usernameExists(member.getUsername())){
            System.out.println("멤버 존재");
            Member targetMember = memberRepository.findByUserName(member.getUsername());
            System.out.println("타겟 멤버 가져옴");
            if(targetMember.getPassword().equals(member.getPassword())){
                System.out.println("비밀번호 일치");
                User user = socketController.getUser();
                Set<Friend> freindSet = targetMember.getFriends();
                for(Friend friend : freindSet){
                    System.out.println(friend.getFriend().getUsername());
                }
                user.login(targetMember);
                try{
                    socketController.sendResponse(new ServerResponse(ProtocolType.LOGIN, true, new LoginResponse(member, "로그인 성공")));
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("로그인 성공 전송");
                ServerSocketController ssc = ServerSocketController.getSocketList().get(ip);
                ServerSocketController.getSocketList().remove(ip);
                ServerSocketController.getSocketList().put(member.getUsername(),ssc);
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
