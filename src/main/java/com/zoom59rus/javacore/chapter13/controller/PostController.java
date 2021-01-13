package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.repository.io.json.JavaIOPostRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.PostRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PostController {
    private final PostRepository postRepository;

    public PostController() {
        this.postRepository = new JavaIOPostRepositoryImpl();
    }

    public Post save(Post post) throws IOException {
        return postRepository.save(post);
    }

    public String save(String content){
        Post post = new Post(null, content, new Date());
        try {
            return save(post).getContent();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Post> saveAll(List<Post> postList){
        try {
            return postRepository.saveAll(postList);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public Post get(Long id) throws IOException {
        return postRepository.get(id).orElse(null);
    }

    public String get(String content){
        Post post = null;
        try {
            post = postRepository.get(content).orElse(null);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return post.getContent();
    }

    public Map<Integer, String> search(String content){
        AtomicInteger count = new AtomicInteger(1);
        Map<Integer, String> find = new HashMap<>();
        try {
            postRepository.getAll().stream()
                    .filter(p -> p.getContent().equals(content))
                    .forEach(p -> find.put(count.getAndIncrement(), p.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return find;
    }

    public List<Post> getAll() throws IOException {
        return postRepository.getAll();
    }

    public List<Post> getPostListByIds(List<Long> postIds){
        return postRepository.getPostListByIds(postIds);
    }

    public String update(String oldContent, String newContent){
        Post post = null;
        try {
            post = postRepository.get(oldContent).orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(oldContent.equals(newContent)){
            return post.getContent();
        }

        post.setContent(newContent);
        post.setUpdated(new Date());

        try {
            post = postRepository.update(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(post != null){
            return post.getContent();
        }

        return null;
    }

    public void remove(Long id) throws IOException {
        postRepository.remove(id);
    }
}
