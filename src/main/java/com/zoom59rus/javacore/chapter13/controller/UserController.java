package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.User;
import com.zoom59rus.javacore.chapter13.repository.JavaIOUserRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.io.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserController {
    private final UserRepository userRepository;

    public UserController() {
        this.userRepository = new JavaIOUserRepositoryImpl();
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public List<User> saveAll(List<User> users){
        try {
            return userRepository.saveAll(users);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public User get(Long id){
        Optional<User> findUser = null;
        try {
            findUser = userRepository.get(id);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return findUser.orElse(null);
    }

    public List<User> getAll(){
        try {
            return userRepository.getAll();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public boolean remove(Long id) throws IOException{
        return userRepository.remove(id);
    }

    public User update(User user) throws IOException {

        return null;
    }


    public User getById(Long id) throws IOException {
        return get(id);
    }

    public User getByFirstName(String firstName) throws IOException {
        Optional<User> findUser = userRepository.getUserByFirstName(firstName);

        return findUser.orElse(null);
    }

    public User getByLastName(String lastName) throws IOException {
        return userRepository.getUserByLastName(lastName).orElse(null);
    }

    public User getByFirstAndLastNames(String firstName, String lastName) throws IOException {
        return userRepository.getUserByFirstAndLastNames(firstName, lastName).orElse(null);
    }

    public List<User> getUsersByRegion(String region) throws IOException {
        return getAll().stream().filter(u -> u.getRegion().equals(region)).collect(Collectors.toList());
    }

    public List<User> getUsersByPost(String post){
        try {
            return userRepository.getUsersByPost(post);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public List<User> getUsersByFirstName(String firstName){
        try {
            return userRepository.getUsersByFirstName(firstName);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public List<User> getUsersByLastName(String lastName){
        try {
            return userRepository.getUsersByLastName(lastName);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public List<User> getUsersByFirstAndLastName(String firstName, String lastName){
        try {
            return userRepository.getUsersByFirstAndLastNames(firstName, lastName);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }
}