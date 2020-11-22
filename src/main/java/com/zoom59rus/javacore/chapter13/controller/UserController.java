package main.java.com.zoom59rus.javacore.chapter13.controller;

import main.java.com.zoom59rus.javacore.chapter13.model.Region;
import main.java.com.zoom59rus.javacore.chapter13.repository.post.JavaIOPostRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.post.PostRepository;
import main.java.com.zoom59rus.javacore.chapter13.repository.region.JavaIORegionRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.region.RegionRepository;
import main.java.com.zoom59rus.javacore.chapter13.repository.user.JavaIOUserRepositoryImpl;
import main.java.com.zoom59rus.javacore.chapter13.repository.user.UserRepository;

import java.io.IOException;
import java.util.List;

public class UserController {
    UserRepository userRepository;
    PostRepository postRepository;
    RegionRepository regionRepository;

    public UserController() throws IOException {
        this.userRepository = new JavaIOUserRepositoryImpl();
        this.postRepository = new JavaIOPostRepositoryImpl();
        this.regionRepository = new JavaIORegionRepositoryImpl();
    }


    public List<Region> getAllRegions() throws IOException {
        return regionRepository.getAll();
    }
}