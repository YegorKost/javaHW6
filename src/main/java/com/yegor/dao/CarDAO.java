package com.yegor.dao;

import com.yegor.entities.Car;
import com.yegor.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Created by YegorKost on 27.02.2017.
 */
public class CarDAO implements DAO<Car> {
    @Override
    public void add(Car car) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.save(car);
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }

    }

    @Override
    public void update(Car car) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.update(car);
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
    public Car getById(int id) {
        Car car = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            car = session.get(Car.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            rollbackTransaction(session);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return car;
    }

    @Override
    public void delete(Car car) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.delete(car);
            session.getTransaction().commit();
        } catch (Exception e ) {
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
