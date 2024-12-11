package com.ltalk.repository;

import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import com.ltalk.enums.FriendStatus;
import com.ltalk.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class FriendRepository {

    public List<Friend> getFriendList(Member member) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Friend> friends = null;
        try {
            System.out.println(member.getUsername());
            friends = em.createQuery("SELECT f FROM Friend f WHERE f.member = :member AND f.status = :status", Friend.class)
                    .setParameter("member", member)
                    .setParameter("status", FriendStatus.ACCEPTED)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
        return friends;
    }

    public void friendRequest(Member member, Member friend) {
        EntityManager em = JpaUtil.getEntityManager();
        Friend friends = new Friend(member, friend, FriendStatus.ACCEPTED); // 친구 관계 객체 생성
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(friends);  // 친구 관계를 나타내는 Friend 객체를 영속성 컨텍스트에 추가
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();  // 예외가 발생하면 트랜잭션 롤백
            }
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);  // EntityManager 종료
        }
    }
}
