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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIOUserRepositoryImpl implements UserRepository {
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/users.json";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private static AtomicLong id;

    public JavaIOUserRepositoryImpl() {
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

        Type list = new TypeToken<List<User>>(){}.getType();
        String json = gson.toJson(userList, list);

        try(FileWriter fw = new FileWriter(path)){
            fw.write(json);
        }

        return user;
    }

    @Override
    public void updateUser(User user) throws IOException {
        remove(user.getId());
        List<User> u = getAll();
        u.add(user);

        Type list = new TypeToken<List<User>>(){}.getType();
        String json = gson.toJson(u, list);

        try(FileWriter fw = new FileWriter(path)){
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

        Type list = new TypeToken<List<User>>(){}.getType();
        String json = gson.toJson(userList, list);

        try(FileWriter fw = new FileWriter(path)){
            fw.write(json);
        }

        return persistUserList;
    }

    @Override
    public User get(Long id) throws IOException {
        return getAll().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User get(String name) throws IOException {
        return getAll().stream()
                .filter(u -> u.getFirstName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getById(Long id) throws IOException {
        return get(id);
    }

    @Override
    public User getUserByFirstName(String firsName) throws IOException {
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
    public User getUsersByPost(Long postId) throws IOException {
        return getAll().stream()
                .filter(u -> u.getPostsId().contains(postId))
                .findFirst()
                .orElse(null);
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
        Type list = new TypeToken<List<User>>(){}.getType();
        List<User> userList = gson.fromJson(loadFromData(), list);
        if(userList == null){
            return new ArrayList<>();
        }

        return userList;
    }

    @Override
    public void remove(Long id) throws IOException {
        List<User> userList = getAll();
        if(userList.isEmpty()){
            return;
        }
        if(!userList.contains(get(id))){
            return;
        }

        userList = userList.stream()
                .filter(u -> !u.getId().equals(id))
                .collect(Collectors.toList());

        Type list = new TypeToken<List<User>>(){}.getType();
        String json = gson.toJson(userList, list);
        try(FileWriter fw = new FileWriter(path)){
            fw.write(json);
        }

    }

    private String loadFromData() throws IOException{
        try(InputStream is = new FileInputStream(path)){
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }
}
