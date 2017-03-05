package com.yegor.dao;

import com.yegor.entities.Mechanic;
import com.yegor.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Created by YegorKost on 27.02.2017.
 */
public class MechanicDAO implements DAO<Mechanic> {
    @Override
    public void add(Mechanic mechanic) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.save(mechanic);
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void update(Mechanic mechanic) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.update(mechanic);
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }

    @Override
    public Mechanic getById(int id) {
        Mechanic mechanic = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            mechanic = session.get(Mechanic.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return mechanic;
    }

    @Override
    public void delete(Mechanic mechanic) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.delete(mechanic);
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }

    private void rollbackTransaction(Session session) {
        if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            session.getTransaction().rollback();
        }
    }
}
