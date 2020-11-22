package main.java.com.zoom59rus.javacore.chapter13.repository;

import main.java.com.zoom59rus.javacore.chapter13.model.BaseClass;

import java.io.IOException;
import java.util.List;

public interface GenericRepository<T extends BaseClass, ID> {

    T save(T t) throws IOException;
    T get(ID id) throws IOException;
    T get(String name) throws IOException;
    List<T> getAll() throws IOException;
    T searchById(ID id) throws IOException;
    T searchByName(String name) throws IOException;
    boolean isExist(String name) throws IOException;
    void remove(ID id);

}