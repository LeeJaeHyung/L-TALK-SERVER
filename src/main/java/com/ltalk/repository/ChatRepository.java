package com.ltalk.repository;

import com.ltalk.entity.Chat;
import com.ltalk.entity.Member;
import com.ltalk.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ChatRepository {

    public boolean save(Chat chat) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        System.out.println(chat.getSend_date()==null);
        System.out.println(chat.getSend_date());
        try {
            transaction.begin();
            em.persist(chat);
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
