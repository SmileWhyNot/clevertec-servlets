package com.example.clevertecservlets.repository;

import java.util.Optional;

public interface CRUDRepository<T> {

    Optional<T> create(T t);

    Optional<T> update(T t);

    Optional<T> getById(Long id);

    boolean deleteById(Long id);

    boolean isNameUnique(String name);
}
