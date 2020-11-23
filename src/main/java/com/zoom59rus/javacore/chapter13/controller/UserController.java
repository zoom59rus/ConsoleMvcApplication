package main.java.com.zoom59rus.javacore.chapter13.controller;

import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;
import main.java.com.zoom59rus.javacore.chapter13.model.User;
import main.java.com.zoom59rus.javacore.chapter13.repository.post.JavaIOPostRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.post.PostRepository;
import main.java.com.zoom59rus.javacore.chapter13.repository.region.JavaIORegionRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.region.RegionRepository;
import main.java.com.zoom59rus.javacore.chapter13.repository.user.JavaIOUserRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.user.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {
    UserRepository userRepository;
    PostRepository postRepository;
    RegionRepository regionRepository;

    public UserController(){
        this.userRepository = new JavaIOUserRepositoryImpl();
        this.postRepository = new JavaIOPostRepositoryImpl();
        this.regionRepository = new JavaIORegionRepositoryImpl();
    }

    public User save(String firstName, String lastName, List<Post> postList, Region region) {
        List<Post> persistPostList = postList.stream().map(this::save).collect(Collectors.toList());
        Region persistRegion = save(region);
        User persistUser = new User(null, firstName, lastName, persistPostList, persistRegion);

        try {
            userRepository.save(persistUser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return persistUser;
    }

    public Region save(Region region) {
        Region persist = null;
        try {
            persist = regionRepository.save(region);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }

        return persist;
    }

    public Post save(Post post) {
        Post persist = null;
        try {
            persist = postRepository.save(post);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }

        return persist;
    }
}