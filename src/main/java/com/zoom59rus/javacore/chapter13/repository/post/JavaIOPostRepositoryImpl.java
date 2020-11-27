package com.zoom59rus.javacore.chapter13.repository.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zoom59rus.javacore.chapter13.model.Post;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIOPostRepositoryImpl implements PostRepository{
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/posts.json";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private volatile AtomicLong id;

    public JavaIOPostRepositoryImpl() {
    }

    private AtomicLong getNextId() throws IOException {
        AtomicLong id = new AtomicLong(1);

        List<Post> postList = getAll();
        if(postList.isEmpty()){
            return id;
        }

        for (Post post : postList) {
            if(post.getId() > id.get()){
                id.set(post.getId());
            }
        }
        id.incrementAndGet();

        return id;
    }

    private Long getId() throws IOException {
        if(id == null){
            id = getNextId();
        }

        return id.getAndIncrement();
    }

    @Override
    public Post save(Post post) throws IOException {
        List<Post> postList = getAll();
        if(postList.contains(post)){
            return postList.stream()
                    .filter(p -> p.equals(post))
                    .findFirst().orElse(post);
        }

        post.setId(getId());
        postList.add(post);

        Type list = new TypeToken<List<Post>>(){}.getType();
        String json = gson.toJson(postList, list);

        try(FileWriter fw = new FileWriter(path)){
            fw.write(json);
        }
        return post;
    }

    @Override
    public List<Post> saveAll(List<Post> lists) throws IOException {
        lists = lists.stream()
                .peek(p -> {
                    try {
                        p.setId(getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());

        List<Post> postList = getAll();
        postList.addAll(lists);

        Type list = new TypeToken<List<Post>>(){}.getType();
        String json = gson.toJson(postList, list);

        try(FileWriter fw = new FileWriter(path)){
            fw.write(json);
        }

        return lists;
    }

    @Override
    public Post get(Long id) throws IOException {

        return getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Post get(String content) throws IOException {

        return getAll().stream()
                .filter(p -> p.getContent().equals(content))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Post> getAll() throws IOException {
        String json = loadFileData();
        Type list = new TypeToken<List<Post>>(){}.getType();
        List<Post> postList = gson.fromJson(json, list);
        if(postList == null){
            return new ArrayList<>();
        }

        return postList;
    }

    @Override
    public List<Post> getPostListByIds(List<Long> postIds){
        List<Post> p = postIds.stream().map(post -> {
            try {
                return get(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return p;
    }

    @Override
    public void remove(Long id) throws IOException {
        List<Post> postList = getAll();
        if(postList.isEmpty()){
            return;
        }

        postList = postList.stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());

        Type list = new TypeToken<List<Post>>(){}.getType();
        String json = gson.toJson(postList, list);
        try(FileWriter fw = new FileWriter(path)){
            fw.write(json);
        }

    }

    private String loadFileData() throws IOException {
        try(InputStream is = new FileInputStream(path)){
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }
}
