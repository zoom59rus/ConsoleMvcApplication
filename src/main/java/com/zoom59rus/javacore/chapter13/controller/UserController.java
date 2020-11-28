package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import com.zoom59rus.javacore.chapter13.model.dtos.UserDto;
import com.zoom59rus.javacore.chapter13.repository.user.JavaIOUserRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.user.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
        Optional<User> u = userRepository.getById(id);
        return u.map(user -> UserDto.fromUser(user, postController.getPostListByIds(user.getPostsId())))
                .orElse(null);
    }

    public UserDto getByFirstName(String firstName) throws IOException {
        Optional<User> u = userRepository.getUserByFirstName(firstName);

        return u.map(user -> UserDto.fromUser(user, postController.getPostListByIds(user.getPostsId())))
                .orElse(null);
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
        Post p = postController.get(content);
        if(p != null){
            User u = userRepository.getUsersByPost(p.getId()).orElse(null);
            if(u != null){
                return UserDto.fromUser(u, postController.getPostListByIds(u.getPostsId()));
            }
        }

        return null;
    }

    public List<UserDto> getUsersByRegion(String region) throws IOException {
        List<User> u = userRepository.getUsersByRegion(region);
        if(u.isEmpty()){
            return null;
        }

        List<UserDto> list = u.stream()
                .map(user -> UserDto.fromUser(user, postController.getPostListByIds(user.getPostsId())))
                .collect(Collectors.toList());

        return list;
    }

    public boolean remove(Long id) throws IOException{
        return userRepository.remove(id);
    }

    public void update(UserDto userDto) throws IOException {
        List<Post> postList = postController.saveAll(userDto.getPosts());
        List<Long> postsId = (postList.stream()
                .map(Post::getId)
                .collect(Collectors.toList()));

        User u = UserDto.fromUserDto(userDto, postsId);
        u.setId(userDto.getId());

        userRepository.updateUser(u);
    }
}