package com.zoom59rus.javacore.chapter13.repository.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zoom59rus.javacore.chapter13.repository.PostRepository;
import com.zoom59rus.javacore.chapter13.repository.RegionRepository;
import com.zoom59rus.javacore.chapter13.repository.UserRepository;
import com.zoom59rus.javacore.chapter13.repository.converter.PersistUser;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
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
    private final RegionRepository regionRepository;
    private final PostRepository postRepository;

    private final String sourcePath;
    private final Gson gson;
    private final Type objectsType;
    private volatile AtomicLong id;

    public JavaIOUserRepositoryImpl(){
        this.sourcePath = "src/main/resources/files/users.json";
        this.gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        this.objectsType = new TypeToken<List<PersistUser>>(){}.getType();
        this.regionRepository = new JavaIORegionRepositoryImpl();
        this.postRepository = new JavaIOPostRepositoryImpl();
    }

    @Override
    public User save(User user) throws IOException {
        List<User> savedUserList = getAll();
        if(savedUserList.contains(user)){
            return update(user);
        }

        user.setId(getId());
        user.setPosts(postRepository.saveAll(user.getPosts()));
        user.setRegion(regionRepository.save(user.getRegion()));
        savedUserList.add(user);

        List<PersistUser> savePersistUser = fromUserList(savedUserList);
        String json = gson.toJson(savePersistUser, objectsType);

        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
            return user;
        }
    }

    @Override
    public List<User> saveAll(List<User> lists) throws IOException {
        lists = lists.stream()
                .peek(u -> u.setId(getId()))
                .peek(u -> {
                    try {
                        u.setPosts(postRepository.saveAll(u.getPosts()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .peek(u -> {
                    try {
                        u.setRegion(regionRepository.save(u.getRegion()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());

        List<PersistUser> persistUsers = lists.stream()
                .map(this::fromUser)
                .collect(Collectors.toList());

        String json = gson.toJson(persistUsers, objectsType);
        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
            return lists;
        }
    }

    @Override
    public Optional<User> get(Long id)throws IOException{

        return getAllPersist().stream()
                .filter(p -> p.getId().equals(id))
                .map(p -> {
                    try {
                        return fromPersistUser(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .findAny();
    }

    @Override
    public List<User> getAll() throws IOException {
        return getAllPersist().stream()
                .map(p -> {
                    try {
                        return fromPersistUser(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean remove(Long id) throws IOException {
        List<PersistUser> persistUsers = getAllPersist();
        if(persistUsers.isEmpty()){
            return false;
        }

        persistUsers.stream()
                .filter(u -> u.getId().equals(id))
                .peek(u -> u.getPostsId().forEach(p -> {
                    try {
                        postRepository.remove(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }))
                .collect(Collectors.toList());

        persistUsers = persistUsers.stream()
                .filter(u -> !u.getId().equals(id))
                .collect(Collectors.toList());

        String json = gson.toJson(persistUsers, objectsType);
        try(FileWriter fw = new FileWriter(sourcePath)){
            fw.write(json);
            return true;
        }
    }

    @Override
    public User update(User user) throws IOException {
        User savedUser = getAll().stream()
                .filter(u -> u.equals(user))
                .findFirst()
                .orElse(null);
        if(savedUser == null){
            throw new IOException("Не удалось получить существующую запись пользователя.");
        }
        List<Post> post = postRepository.saveAll(user.getPosts());
        savedUser.getPosts().addAll(post);
        if(remove(savedUser.getId())){
            List<PersistUser> savePersistUser = getAllPersist();
            savePersistUser.add(fromUser(savedUser));
            String json = gson.toJson(savePersistUser, objectsType);
            try(FileWriter fw = new FileWriter(sourcePath)){
                fw.write(json);
                return user;
            }
        }

        return null;
    }

    @Override
    public Optional<User> getById(Long id) throws IOException {
        return get(id);
    }

    @Override
    public Optional<User> getUserByFirstName(String firsName) throws IOException {
        return getAllPersist().stream()
                .filter(p -> p.getFirstName().equals(firsName))
                .map(p -> {
                    try {
                        return fromPersistUser(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .findAny();
    }

    @Override
    public Optional<User>  getUserByLastName(String lastName) throws IOException {
        return getAllPersist().stream()
                .filter(p -> p.getFirstName().equals(lastName))
                .map(p -> {
                    try {
                        return fromPersistUser(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .findAny();
    }

    @Override
    public Optional<User>  getUserByFirstAndLastNames(String firstName, String lastName) throws IOException {

        return getAllPersist().stream()
                .filter(u -> u.getFirstName().equals(firstName))
                .filter(u -> u.getLastName().equals(lastName))
                .map(p -> {
                    try {
                        return fromPersistUser(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .findFirst();
    }

    @Override
    public List<User> getUsersByRegion(String region) throws IOException {
        Optional<Region> searchRegion = regionRepository.get(region);
        if(searchRegion.isPresent()){
            Region reg = searchRegion.get();
            System.out.println(reg);

            return getAllPersist().stream()
                    .filter(r -> r.getRegionId() == r.getId())
                    .map(rr -> {
                        try {
                            return fromPersistUser(rr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public List<User> getUsersByPost(String post) throws IOException {
        Optional<Post> searchPost = postRepository.get(post);
        if(searchPost.isPresent()){
            Post p = searchPost.get();
            return getAllPersist().stream()
                    .filter(persistUser -> persistUser.getPostsId().contains(p.getId()))
                    .map(pos -> {
                        try {
                            return fromPersistUser(pos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .collect(Collectors.toList());

        }

        return null;
    }

    @Override
    public List<User> getUsersByFirstName(String firsName) throws IOException {
        return fromPersistUsers(getAllPersist().stream()
                                                .filter(p -> p.getFirstName().equals(firsName))
                                                .collect(Collectors.toList()));
    }

    @Override
    public List<User> getUsersByLastName(String lastName) throws IOException {
        return fromPersistUsers(getAllPersist().stream()
                .filter(p -> p.getFirstName().equals(lastName))
                .collect(Collectors.toList()));
    }

    @Override
    public List<User> getUsersByFirstAndLastNames(String firstName, String lastName) throws IOException {
        return getAllPersist().stream()
                .filter(p -> p.getFirstName().equals(firstName))
                .filter(pe -> pe.getLastName().equals(lastName))
                .map(per -> {
                    try {
                        return fromPersistUser(per);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    private String loadFromData() throws IOException{
        try(InputStream is = new FileInputStream(sourcePath)){
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }

    private List<PersistUser> getAllPersist() throws IOException {
        List<PersistUser> persistUsers = gson.fromJson(loadFromData(), objectsType);
        if(persistUsers == null){
            return new ArrayList<>();
        }

        return persistUsers;
    }

    private User fromPersistUser(PersistUser persistUser) throws IOException {
        Region region = regionRepository.get(persistUser.getRegionId()).orElse(null);
        List<Post> postList = postRepository.getPostListByIds(persistUser.getPostsId());
        return  new User(persistUser.getId(),
                persistUser.getFirstName(),
                persistUser.getLastName(),
                postList, region
        );
    }

    private List<User> fromPersistUsers(List<PersistUser> persistUsers){
        return persistUsers.stream().map(p -> {
            try {
                return fromPersistUser(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    private PersistUser fromUser(User user){
        return new PersistUser(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPosts().stream().map(Post::getId).collect(Collectors.toList()),
                user.getRegion().getId()
                );
    }

    private List<PersistUser> fromUserList(List<User> users){
        return users.stream().map(this::fromUser).collect(Collectors.toList());
    }

    private AtomicLong getNextId() throws IOException {
        AtomicLong id = new AtomicLong(1);

        List<PersistUser> persistUsers = getAllPersist();
        if(persistUsers.isEmpty()){
            return id;
        }

        for (PersistUser persist : persistUsers) {
            if(persist.getId() > id.get()){
                id.set(persist.getId());
            }
        }

        id.incrementAndGet();
        return id;
    }

    synchronized private Long getId(){
        if(id == null){
            try {
                id = getNextId();
            } catch (IOException e) {
                System.err.println("Ошибка в получении id.");
            }
        }

        return id.getAndIncrement();
    }
}
