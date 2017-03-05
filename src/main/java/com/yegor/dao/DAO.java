package com.yegor.dao;

/**
 * Created by YegorKost on 27.02.2017.
 */
public interface DAO<E> {
    void add(E e);
    void update(E e);
    E getById(int id);
    void delete(E e);
}
