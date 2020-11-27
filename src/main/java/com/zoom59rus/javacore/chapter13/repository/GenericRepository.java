package com.zoom59rus.javacore.chapter13.repository;

import java.io.IOException;
import java.util.List;

public interface GenericRepository<T, ID extends Number> {

    T save(T t) throws IOException;
    List<T> saveAll(List<T> lists) throws IOException;
    T get(ID id) throws IOException;
    T get(String name) throws IOException;
    List<T> getAll() throws IOException;
    void remove(ID id) throws IOException;
}