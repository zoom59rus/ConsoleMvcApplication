package main.java.com.zoom59rus.javacore.chapter13.repository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public interface GenericRepository<T, ID extends Number> {

    AtomicLong getNextId() throws IOException;
    T save(T t) throws IOException;
    void saveAll(List<T> lists);
    T get(ID id) throws IOException;
    T get(String name) throws IOException;
    List<T> getAll() throws IOException;
    void remove(ID id);
}