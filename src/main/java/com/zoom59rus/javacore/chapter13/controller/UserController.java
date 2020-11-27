package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import com.zoom59rus.javacore.chapter13.model.dtos.UserDto;
import com.zoom59rus.javacore.chapter13.repository.user.JavaIOUserRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.user.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {
    private final RegionController regionController;
    private final PostController postController;
    private final UserRepository userRepository;

    public UserController() {
        this.regionController = new RegionController();
        this.postController = new PostController();
        this.userRepository = new JavaIOUserRepositoryImpl();
    }

    public void save(UserDto userDto) throws IOException {
        List<Post> postList = postController.saveAll(userDto.getPosts());
        List<Long> postsId = (postList.stream()
                .map(Post::getId)
                .collect(Collectors.toList()));

        Region region = regionController.save(userDto.getRegion());

        User persistUser = new User(null, userDto.getFirstName(), userDto.getLastName(), postsId, region);
        userRepository.save(persistUser);
    }

    public UserDto getById(Long id) throws IOException {
        User u = userRepository.getById(id);

        return UserDto.fromUser(u, postController.getPostListByIds(u.getPostsId()));
    }

    public UserDto getByFirstName(String firstName) throws IOException {
        User u = userRepository.getUserByFirstName(firstName);

        return UserDto.fromUser(u, postController.getPostListByIds(u.getPostsId()));
    }

    public UserDto getByLastName(String lastName) throws IOException {
        User u = userRepository.getUserByLastName(lastName);

        return UserDto.fromUser(u, postController.getPostListByIds(u.getPostsId()));
    }

    public UserDto getByFirstAndLastNames(String firstName, String lastName) throws IOException {
        User u = userRepository.getUserByFirstAndLastNames(firstName, lastName);

        return UserDto.fromUser(u, postController.getPostListByIds(u.getPostsId()));
    }

    public UserDto getByContent(String content) throws IOException {
        Post p = postController.getFirstContent(content);
        User u = userRepository.getUsersByPost(p.getId());

        return UserDto.fromUser(u, postController.getPostListByIds(u.getPostsId()));
    }

    public UserDto getByRegion(String region) throws IOException {
        User u = userRepository.getUsersByRegion(region).stream().findFirst().orElse(null);
        if(u == null){
            return null;
        }

        return UserDto.fromUser(u, postController.getPostListByIds(u.getPostsId()));
    }

    public void remove(Long id) throws IOException{
        userRepository.remove(id);
    }

    public void update(UserDto userDto){

    }
}