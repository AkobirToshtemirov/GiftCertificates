package com.epam.esm.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    T save(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    List<T> findAllWithPage(int page, int size);

    void delete(Long id);
}
