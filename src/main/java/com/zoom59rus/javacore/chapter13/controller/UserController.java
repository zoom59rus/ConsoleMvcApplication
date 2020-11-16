package main.java.com.zoom59rus.javacore.chapter13.controller;

import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;
import main.java.com.zoom59rus.javacore.chapter13.model.User;
import main.java.com.zoom59rus.javacore.chapter13.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserController {

    public static User saveUser(String firstName, String lastName,
                               List<Post> postList, Region region){

//        Long id = UserRepository.getNextId();

//        List<Post> posts = postList.stream()
//                .map(post -> new Post(UserRepository.getNextId(), post))
//                .collect(Collectors.toList());

        User user = new User(1L, firstName, lastName, postList, region);

//        Optional<User> savedUser = UserRepository.save(user);

        return user;
    }
}