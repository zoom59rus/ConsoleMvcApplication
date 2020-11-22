package main.java.com.zoom59rus.javacore.chapter13.controller;

import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;
import main.java.com.zoom59rus.javacore.chapter13.model.User;
import main.java.com.zoom59rus.javacore.chapter13.model.dtos.UserDto;
import main.java.com.zoom59rus.javacore.chapter13.repository.post.JavaIOPostRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.post.PostRepository;
import main.java.com.zoom59rus.javacore.chapter13.repository.region.JavaIORegionRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.region.RegionRepository;
import main.java.com.zoom59rus.javacore.chapter13.repository.user.JavaIOUserRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.user.UserRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {
    UserRepository userRepository;
    PostRepository postRepository;
    RegionRepository regionRepository;

    public UserController() throws IOException {
        this.userRepository = new JavaIOUserRepositoryImpl();
        this.postRepository = new JavaIOPostRepositoryImpl();
        this.regionRepository = new JavaIORegionRepositoryImpl();
    }

    public User save(String firstName, String lastName, List<String> posts, String region){
        User savedUser;
        Region r = save(new Region(null, region));
        List<Post> postsList = posts.stream()
                .map(p -> save(new Post(null, p, new Date())))
                .collect(Collectors.toList());

        User persist = new User(null, firstName, lastName, postsList, r);
        try {
            savedUser = userRepository.save(persist);
            return savedUser;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Region save(Region region){
        Region persist = null;
        try {
            persist = regionRepository.save(region);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }

        return persist;
    }

    public Post save(Post post){
        Post persist = null;
        try {
            persist = postRepository.save(post);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }

        return persist;
    }




    public List<Region> getAllRegions() throws IOException {
        return regionRepository.getAll();
    }
}