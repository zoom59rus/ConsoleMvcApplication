package main.java.com.zoom59rus.javacore.chapter13.repository.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.User;
import main.java.com.zoom59rus.javacore.chapter13.model.dtos.UserDto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIOUserRepositoryImpl implements UserRepository {
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/users.txt";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private AtomicLong currentId;

    public JavaIOUserRepositoryImpl(){
    }

    public AtomicLong getNextId() throws IOException {
        AtomicLong id = new AtomicLong(1);
        if (Files.size(Paths.get(path)) != 0) {
            try (FileReader fReader = new FileReader(path);
                 JsonReader jReader = gson.newJsonReader(fReader)
            ) {
                while (fReader.ready()) {
                    Post post = gson.fromJson(jReader, Post.class);
                    if (post.getId() < id.get()) {
                        continue;
                    }
                    id.set(post.getId());
                }

                currentId = new AtomicLong(id.incrementAndGet());
                return currentId;
            } catch (FileNotFoundException e) {
                System.err.print(e.getMessage());
            }
        }

        currentId = id;

        return currentId;
    }

    @Override
    public User save(User user) throws IOException {

        if (currentId == null) {
            user.setId(getNextId().getAndIncrement());
        } else {
            user.setId(currentId.getAndIncrement());
        }

        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(gson.toJson(UserDto.fromUser(user)) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User get(Long id) throws IOException {
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader);
        ){
            while (fReader.ready()){
                User u = gson.fromJson(jReader, User.class);
                if(u.getId().equals(id)){
                    return u;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return null;
    }

    @Override
    public User get(String name) throws IOException {
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader);
        ){
            while (fReader.ready()){
                User u = gson.fromJson(jReader, User.class);
                if(u.getFirstName().equals(name)){
                    return u;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return null;
    }

    @Override
    public void remove(Long id) {
        List<User> userList = new ArrayList<>();
        try {
            userList = getAll();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
        if(userList.size() == 0){
            return;
        }

        List<User> result = userList.stream()
                .filter(r -> !r.getId().equals(id))
                .collect(Collectors.toList());

        saveAll(result);
    }

    public void saveAll(List<User> userList){
        try (FileWriter fileWriter = new FileWriter(path)) {
            for (User r : userList) {
                try {
                    fileWriter.write(gson.toJson(UserDto.fromUser(r)) + "\n");
                } catch (IOException e) {
                    System.err.print(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAll() throws IOException {
        List<User> userList = new ArrayList<>();
        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = gson.newJsonReader(fileReader)
        ){
            while (fileReader.ready()) {
                userList.add(gson.fromJson(jsonReader, User.class));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return userList;
    }
}
