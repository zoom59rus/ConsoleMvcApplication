package com.zoom59rus.javacore.chapter13.repository.user;

import com.zoom59rus.javacore.chapter13.model.User;
import com.zoom59rus.javacore.chapter13.repository.GenericRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {

    Optional<User> getById(Long id) throws IOException;
    Optional<User> getUserByFirstName(String firsName) throws IOException;
    User getUserByLastName(String lastName) throws IOException;
    User getUserByFirstAndLastNames(String firstName, String lastName) throws IOException;
    Optional<User> getUsersByPost(Long postId) throws IOException;

    List<User> getUsersByName(String firsName) throws IOException;
    List<User> getUsersByLastName(String lastName) throws IOException;
    List<User> getUsersByFirstAndLastNames(String firstName, String lastName) throws IOException;
    List<User> getUsersByRegion(String region) throws IOException;

    void updateUser(User user) throws IOException;

}