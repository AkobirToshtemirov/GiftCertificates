package com.epam.esm.service;

import java.util.List;

public interface BaseService<T> {
    T create(T entity);

    List<T> findAll();

    T findById(Long id);

    void delete(Long id);
}
