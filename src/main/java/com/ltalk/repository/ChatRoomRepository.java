package com.ltalk.repository;

import com.ltalk.entity.Chat;
import com.ltalk.entity.ChatRoom;
import com.ltalk.entity.Member;
import com.ltalk.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class ChatRoomRepository {
    public boolean save(ChatRoom chatRoom) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(chatRoom);
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
    public ChatRoom findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        ChatRoom chatRoom = null;
        try{
            TypedQuery<ChatRoom> query = em.createQuery("SELECT r FROM ChatRoom r WHERE r.id = :id", ChatRoom.class);
            query.setParameter("id", id);
            chatRoom = query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JpaUtil.closeEntityManager(em);
        }
        return chatRoom;
    }
}
