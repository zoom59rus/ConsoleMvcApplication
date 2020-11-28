package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.repository.post.JavaIOPostRepositoryImpl;

import java.io.IOException;
import java.util.List;

public class PostController {
    private final JavaIOPostRepositoryImpl postRepository;

    public PostController() {
        this.postRepository = new JavaIOPostRepositoryImpl();
    }

    public Post save(Post post) throws IOException {
        return postRepository.save(post);
    }

    public List<Post> saveAll(List<Post> postList) throws IOException {
        return postRepository.saveAll(postList);
    }

    public Post get(Long id) throws IOException {
        return postRepository.get(id).orElse(null);
    }

    public Post get(String content) throws IOException {
        return postRepository.get(content).orElse(null);
    }

    public List<Post> getAll() throws IOException {
        return postRepository.getAll();
    }

    public List<Post> getPostListByIds(List<Long> postIds){
        return postRepository.getPostListByIds(postIds);
    }

    public void remove(Long id) throws IOException {
        postRepository.remove(id);
    }
}
