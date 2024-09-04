package com.ltalk.repository;

import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import com.ltalk.enums.FriendStatus;
import com.ltalk.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class FriendRepository {

    public List<Friend> getFriendList(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Friend> friends = null;
        try {
            System.out.println(username);
            friends = em.createQuery("SELECT f FROM Friend f WHERE f.username = :username AND f.status = :status", Friend.class)
                    .setParameter("username", username)
                    .setParameter("status", FriendStatus.ACCEPTED)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
        return friends;
    }

    public void friendRequest(Member member, String friend_name) {
        EntityManager em = JpaUtil.getEntityManager();
        Friend friend = new Friend(member,"as", FriendStatus.ACCEPTED);
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(friend);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }

    }
}
