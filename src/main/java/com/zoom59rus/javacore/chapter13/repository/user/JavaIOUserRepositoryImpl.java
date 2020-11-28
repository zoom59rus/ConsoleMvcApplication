package com.zoom59rus.javacore.chapter13.repository.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zoom59rus.javacore.chapter13.model.User;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIOUserRepositoryImpl implements UserRepository {
    private final String sourcePath;
    private final Gson gson;
    private final Type objectsType;
    private volatile AtomicLong id;

    public JavaIOUserRepositoryImpl(){
        this.sourcePath = "src/main/resources/files/users.json";
        this.gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        this.objectsType = new TypeToken<List<User>>(){}.getType();
    }

    private AtomicLong getNextId() throws IOException {
        AtomicLong id = new AtomicLong(1);
        List<User> userList = getAll();
        if(userList.isEmpty()){
            return id;
        }

        for (User user : userList) {
            if(user.getId() > id.get()){
                id.set(user.getId());
            }
        }

        id.incrementAndGet();
        return id;
    }

    synchronized private Long getId() throws IOException {
        if(id == null){
            id = getNextId();
        }

        return id.getAndIncrement();
    }

    @Override
    public User save(User user) throws IOException {
        List<User> userList = getAll();
        user.setId(getId());
        userList.add(user);

        String json = gson.toJson(userList, objectsType);

        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
        }

        return user;
    }

    @Override
    public void updateUser(User user) throws IOException {
        remove(user.getId());

        List<User> u = getAll();
        u.add(user);

        String json = gson.toJson(u, objectsType);

        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
        }
    }

    @Override
    public List<User> saveAll(List<User> lists) throws IOException {
        List<User> persistUserList = lists.stream()
                .peek(u -> {
                    try {
                        u.setId(getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());

        List<User> userList = getAll();
        userList.addAll(persistUserList);

        String json = gson.toJson(userList, objectsType);

        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
        }

        return persistUserList;
    }

    @Override
    public Optional<User> get(Long id){
        Optional<User> user = null;
        try {
            user = getAll().stream()
                    .filter(u -> u.getId() == id)
                    .findAny();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }

        return user;
    }

    @Override
    public Optional<User> get(String name) throws IOException {
        return getAll().stream()
                .filter(u -> u.getFirstName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<User> getById(Long id) throws IOException {
        return get(id);
    }

    @Override
    public Optional<User> getUserByFirstName(String firsName) throws IOException {
        return get(firsName);
    }

    @Override
    public User getUserByLastName(String lastName) throws IOException {
        return getAll().stream()
                .filter(u -> u.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getUserByFirstAndLastNames(String firstName, String lastName) throws IOException {

        return getAll().stream()
                .filter(u -> u.getFirstName().equals(firstName))
                .filter(u -> u.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getUsersByRegion(String region) throws IOException {
        return getAll().stream()
                .filter(u -> u.getRegion().getName().equals(region))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUsersByPost(Long postId) throws IOException {
        return getAll().stream()
                .filter(u -> u.getPostsId().contains(postId))
                .findFirst();
    }

    @Override
    public List<User> getUsersByName(String firsName) throws IOException {
        return getAll().stream()
                .filter(u -> u.getFirstName().equals(firsName))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersByLastName(String lastName) throws IOException {
        return getAll().stream()
                .filter(u -> u.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersByFirstAndLastNames(String firstName, String lastName) throws IOException {
        return getUsersByName(firstName).stream()
                .filter(u -> u.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() throws IOException {
        List<User> userList = gson.fromJson(loadFromData(), objectsType);
        if(userList == null){
            return new ArrayList<>();
        }

        return userList;
    }

    @Override
    public boolean remove(Long id) throws IOException {
        List<User> userList = getAll();
        if(userList.isEmpty()){
            return false;
        }

        userList = userList.stream()
                .filter(u -> !u.getId().equals(id))
                .collect(Collectors.toList());

        String json = gson.toJson(userList, objectsType);
        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
        }

        return true;
    }

    private String loadFromData() throws IOException{
        try(InputStream is = new FileInputStream(sourcePath)){
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }
}
