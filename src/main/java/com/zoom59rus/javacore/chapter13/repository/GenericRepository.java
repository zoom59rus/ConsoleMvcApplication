package com.zoom59rus.javacore.chapter13.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    T save(T t) throws IOException;
    List<T> saveAll(List<T> lists) throws IOException;
    Optional<T> get(ID id) throws IOException;
    List<T> getAll() throws IOException;
    boolean remove(ID id) throws IOException;
    T update(T t) throws IOException;
}