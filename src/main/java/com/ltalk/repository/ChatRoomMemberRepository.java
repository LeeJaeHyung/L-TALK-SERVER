package com.ltalk.repository;

import com.ltalk.entity.ChatRoom;
import com.ltalk.entity.ChatRoomMember;
import com.ltalk.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ChatRoomMemberRepository {
    public boolean save(ChatRoomMember chatRoommember) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(chatRoommember);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            JpaUtil.closeEntityManager(em);
        }
        return true;
    }
}
