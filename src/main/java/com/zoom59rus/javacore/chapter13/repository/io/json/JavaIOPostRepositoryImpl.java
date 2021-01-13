package com.zoom59rus.javacore.chapter13.repository.io.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.repository.PostRepository;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIOPostRepositoryImpl implements PostRepository {
    private final String sourcePath;
    private final Gson gson;
    private final Type objectsType;
    private volatile AtomicLong id;

    public JavaIOPostRepositoryImpl(){
        this.sourcePath = "src/main/resources/files/posts.json";
        this.gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        this.objectsType = new TypeToken<List<Post>>(){}.getType();
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

        String json = gson.toJson(postList, objectsType);

        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
        }
        return post;
    }

    @Override
    public List<Post> saveAll(List<Post> lists) throws IOException {
        List<Post> postList = getAll();

        lists = lists.stream()
                .peek(p -> {
                    try {
                        p.setId(getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());

        postList.addAll(lists);
        String json = gson.toJson(postList, objectsType);

        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
        }

        return lists;
    }

    @Override
    public Optional<Post> get(Long id) throws IOException {

        return getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Post> get(String content) throws IOException {

        return getAll().stream()
                .filter(p -> p.getContent().equals(content))
                .findFirst();
    }

    @Override
    public List<Post> getAll() throws IOException {
        String json = loadFileData();
        List<Post> postList = gson.fromJson(json, objectsType);
        if(postList == null){
            return new ArrayList<>();
        }

        return postList;
    }

    @Override
    public List<Post> getPostListByIds(List<Long> postIds){
        List<Post> p = postIds.stream().map(post -> {
            try {
                return get(post).orElse(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return p;
    }

    @Override
    public boolean remove(Long id) throws IOException {
        List<Post> postList = getAll();
        if(postList.isEmpty()){
            return false;
        }

        postList = postList.stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());

        String json = gson.toJson(postList, objectsType);
        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
        }

        return true;
    }

    @Override
    public Post update(Post post) throws IOException {
        if(remove(post.getId())){
            List<Post> postList = getAll();
            postList.add(post);
            String json = gson.toJson(postList, objectsType);
            try(FileWriter fw = new FileWriter(sourcePath)){
                fw.write(json);
                return post;
            }
        }

        return null;
    }

    private String loadFileData() throws IOException {
        try(InputStream is = new FileInputStream(sourcePath)){
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }
}
