package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import com.zoom59rus.javacore.chapter13.repository.io.csv.CsvUserRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.io.json.JavaIOUserRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserController {
    private final UserRepository userRepository;

    public UserController() {
        this.userRepository = CsvUserRepositoryImpl.getInstance();
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public List<User> saveAll(List<User> users) {
        try {
            return userRepository.saveAll(users);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public User get(Long id) {
        Optional<User> findUser = null;
        try {
            findUser = userRepository.get(id);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return findUser.orElse(null);
    }

    public List<User> getAll() {
        try {
            return userRepository.getAll();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public boolean remove(Long id) {
        try {
            return userRepository.remove(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean remove(User user) {
        Optional<User> findUser = null;
        try {
            findUser = userRepository.getUserByFirstName(user.getFirstName());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return findUser.filter(value -> remove(value.getId())).isPresent();
    }

    public User update(User user){
        if(remove(user)){
            return save(user);
        }else return null;
    }

    public User getById(Long id) throws IOException {
        return get(id);
    }

    public User getByFirstName(String firstName) {
        Optional<User> findUser = null;
        try {
            findUser = userRepository.getUserByFirstName(firstName);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (findUser.isPresent()) {
            User u = findUser.get();
            u.setId(null);
            return u;
        } else return null;
    }

    public User getByLastName(String lastName) {
        Optional<User> findUser = null;
        try {
            findUser = userRepository.getUserByLastName(lastName);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (findUser.isPresent()) {
            User u = findUser.get();
            u.setId(null);
            return u;
        } else return null;
    }

    public User getByFirstAndLastNames(String firstName, String lastName) {
        Optional<User> findUser = null;
        try {
            findUser = userRepository.getUserByFirstAndLastNames(firstName, lastName);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (findUser.isPresent()) {
            User u = findUser.get();
            u.setId(null);
            return u;
        } else return null;
    }

    public List<User> getUsersByRegion(String region) {
        return getAll().stream()
                .filter(u -> u.getRegion().equals(region))
                .peek(u -> u.setId(null))
                .collect(Collectors.toList());
    }

    public List<User> getUsersByPost(String post) {
        try {
            return userRepository.getUsersByPost(post).stream()
                    .peek(u -> u.setId(null))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public List<User> getUsersByFirstName(String firstName) {
        try {
            return userRepository.getUsersByFirstName(firstName).stream()
                    .peek(u -> u.setId(null))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public List<User> getUsersByLastName(String lastName) {
        try {
            return userRepository.getUsersByLastName(lastName).stream()
                    .peek(u -> u.setId(null))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public List<User> getUsersByFirstAndLastName(String firstName, String lastName) {
        try {
            return userRepository.getUsersByFirstAndLastNames(firstName, lastName).stream()
                    .peek(u -> u.setId(null))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public boolean createUser(String firstName, String lastName, List<String> contents, String region) {
        List<Post> postList = contents.stream()
                .map(p -> new Post(null, p, new Date()))
                .collect(Collectors.toList());
        Region p = new Region(null, region);
        User persistUser = new User(null, firstName, lastName, postList, p);

        persistUser = save(persistUser);

        if (persistUser != null) {
            persistUser.setId(null);
            return true;
        } else return false;
    }
}