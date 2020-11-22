package main.java.com.zoom59rus.javacore.chapter13.repository.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.com.zoom59rus.javacore.chapter13.model.BaseClass;
import main.java.com.zoom59rus.javacore.chapter13.model.User;
import main.java.com.zoom59rus.javacore.chapter13.model.dtos.UserDto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaIOUserRepositoryImpl implements UserRepository {
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/users.txt";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private Long currentId;

    public JavaIOUserRepositoryImpl() throws IOException {
    }

    public Long getNextId() throws IOException {
        Long id = 1L;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                UserDto userDto = gson.fromJson(jReader, UserDto.class);
                if (userDto.getId() < id) {
                    continue;
                }
                id = userDto.getId() + 1;
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        currentId = id;

        return id;
    }

    @Override
    public User save(User user) throws IOException {
        if (isExist(user.getFirstName())) {
            return user;
        }

        if (currentId == null) {
            user.setId(getNextId());
        } else {
            user.setId(currentId);
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
        User user = searchById(id);
        if (user != null) {
            return user;
        }

        return null;
    }

    @Override
    public User get(String name) throws IOException {
        User user = searchByName(name);
        if (user != null) {
            return user;
        }

        return null;
    }

    @Override
    public User searchById(Long id) throws IOException {
        User user = null;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                user = gson.fromJson(jReader, User.class);
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return user;
    }

    @Override
    public User searchByName(String name) throws IOException {
        User user = null;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                user = gson.fromJson(jReader, User.class);
                if (user.getFirstName().equals(name)) {
                    return user;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return user;
    }

    @Override
    public boolean isExist(String name) throws IOException {
        User r = searchByName(name);

        return r != null;
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
                    fileWriter.write(gson.toJson(r) + "\n");
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
