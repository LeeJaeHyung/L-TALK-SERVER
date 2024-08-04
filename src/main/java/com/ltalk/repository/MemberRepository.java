package com.ltalk.repository;

import com.ltalk.entity.Member;
import com.ltalk.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class MemberRepository {
    public boolean save(Member member) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(member);
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

    public List<Member> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Member> members = null;
        try {
            members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
        return members;
    }

    public boolean usernameExists(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class);
            query.setParameter("username", username);
            return !query.getResultList().isEmpty();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }

    public boolean emailExists(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class);
            query.setParameter("email", email);
            return !query.getResultList().isEmpty();
        } finally {
            JpaUtil.closeEntityManager(em);
        }
    }


}
