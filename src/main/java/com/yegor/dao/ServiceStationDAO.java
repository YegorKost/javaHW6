package com.yegor.dao;

import com.yegor.entities.ServiceStation;
import com.yegor.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Created by YegorKost on 28.02.2017.
 */
public class ServiceStationDAO implements DAO<ServiceStation> {
    @Override
    public void add(ServiceStation serviceStation) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.save(serviceStation);
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
    public void update(ServiceStation serviceStation) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.update(serviceStation);
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
    public ServiceStation getById(int id) {
        ServiceStation serviceStation = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            serviceStation = session.get(ServiceStation.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return serviceStation;
    }

    @Override
    public void delete(ServiceStation serviceStation) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.delete(serviceStation);
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
