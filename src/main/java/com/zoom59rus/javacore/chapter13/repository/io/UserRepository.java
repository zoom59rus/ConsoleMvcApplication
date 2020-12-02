package com.zoom59rus.javacore.chapter13.repository.io;

import com.zoom59rus.javacore.chapter13.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {

    Optional<User> getById(Long id) throws IOException;
    Optional<User> getUserByFirstName(String firsName) throws IOException;
    Optional<User>  getUserByLastName(String lastName) throws IOException;
    Optional<User>  getUserByFirstAndLastNames(String firstName, String lastName) throws IOException;

    List<User> getUsersByPost(String post) throws IOException;
    List<User> getUsersByFirstName(String firsName) throws IOException;
    List<User> getUsersByLastName(String lastName) throws IOException;
    List<User> getUsersByFirstAndLastNames(String firstName, String lastName) throws IOException;
    List<User> getUsersByRegion(String region) throws IOException;
}