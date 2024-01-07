package com.epam.esm.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    T create(T entity);

    List<T> findAll();

    List<T> findAllWithPage(int page, int size);

    Optional<T> findById(Long id);

    void delete(Long id);
}
