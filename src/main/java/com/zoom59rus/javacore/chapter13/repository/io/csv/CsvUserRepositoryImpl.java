package com.zoom59rus.javacore.chapter13.repository.io.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import com.zoom59rus.javacore.chapter13.repository.PostRepository;
import com.zoom59rus.javacore.chapter13.repository.RegionRepository;
import com.zoom59rus.javacore.chapter13.repository.UserRepository;
import com.zoom59rus.javacore.chapter13.repository.converter.UserCsvConvert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CsvUserRepositoryImpl implements UserRepository {
    private static CsvUserRepositoryImpl INSTANCE;
    private static final String SOURCE_PATH = "src/main/resources/files/csv/user.csv";

    private RegionRepository regionRepository;
    private PostRepository postRepository;
    private volatile AtomicLong id;
    private Lock lock;

    private CsvUserRepositoryImpl() {
        this.regionRepository = CsvRegionRepositoryImpl.getInstance();
        this.postRepository = CsvPostRepositoryImpl.getInstance();
        this.lock = new ReentrantLock();
    }

    public static CsvUserRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (CsvUserRepositoryImpl.class) {
                INSTANCE = new CsvUserRepositoryImpl();
            }
        }

        return INSTANCE;
    }

    @Override
    public Optional<User> getById(Long id) throws IOException {
        return get(id);
    }

    @Override
    public Optional<User> getUserByFirstName(String firsName) throws IOException {
        return getAll().stream()
                .filter(u -> u.getFirstName().equals(firsName))
                .findFirst();
    }

    @Override
    public Optional<User> getUserByLastName(String lastName) throws IOException {
        return getAll().stream()
                .filter(u -> u.getLastName().equals(lastName))
                .findFirst();
    }

    @Override
    public Optional<User> getUserByFirstAndLastNames(String firstName, String lastName) throws IOException {
        return getAll().stream()
                .filter(u -> u.getFirstName().equals(firstName))
                .filter(u -> u.getLastName().equals(lastName))
                .findFirst();
    }

    @Override
    public List<User> getUsersByPost(String post) throws IOException {
        return getAll().stream()
                .filter(u -> u.getPosts().contains(post))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersByFirstName(String firsName) throws IOException {
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
        return getAll().stream()
                .filter(u -> u.getFirstName().equals(firstName))
                .filter(u -> u.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersByRegion(String region) throws IOException {
        return getAll().stream()
                .filter(u -> u.getRegion().getName().equals(region))
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) throws IOException {
        List<UserCsvConvert> userCsvConverts = getAllRecords();
        List<Post> posts = postRepository.saveAll(user.getPosts());
        Region region = regionRepository.save(user.getRegion());
        List<Long> postsIds = posts.stream().map(Post::getId).collect(Collectors.toList());

        UserCsvConvert saveUser = convertUser(user, postsIds, region.getId());
        saveUser.setId(getId());
        userCsvConverts.add(saveUser);

        saveToData(userCsvConverts);

        return user;
    }

    @Override
    public List<User> saveAll(List<User> lists){

        return lists.stream().peek(u -> {
            try {
                save(u);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<User> get(Long id) throws IOException {
        UserCsvConvert find = getAllRecords().stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        if (find == null) {
            return Optional.empty();
        }

        List<Post> posts = postRepository.getPostListByIds(find.getPosts());

        User result = convertCsvUser(find, posts, regionRepository.get(find.getId()).get());

        return Optional.of(result);
    }

    @Override
    public List<User> getAll() throws IOException {
        List<UserCsvConvert> allRecords = getAllRecords();
        List<User> result = new ArrayList<>();

        for (UserCsvConvert allRecord : allRecords) {
            List<Post> posts = postRepository.getPostListByIds(allRecord.getPosts());
            result.add(convertCsvUser(
                    allRecord,
                    posts,
                    regionRepository.get(allRecord.getId()).get()
            ));
        }

        return result;
    }

    @Override
    public boolean remove(Long id) throws IOException {
        List<UserCsvConvert> find = getAllRecords();
        if (find.removeIf(u -> u.getId().equals(id))) {
            saveToData(find);
            return true;
        }
        return false;
    }

    @Override
    public User update(User user) throws IOException {
        User findUser = get(user.getId()).orElse(null);

        if(findUser == null){
            throw new IOException("Не удалось получить существующую запись пользователя.");
        }

        List<UserCsvConvert> allRecords = getAllRecords();
        allRecords.removeIf(u -> u.getId().equals(findUser.getId()));
        save(user);

        return user;
    }

    private UserCsvConvert convertUser(User user, List<Long> postId, Long regionId) {
        return UserCsvConvert.convertUser(user, postId, regionId);
    }

    private User convertCsvUser(UserCsvConvert userCsvConvert, List<Post> postList, Region region) {
        return UserCsvConvert.convertCsvUser(userCsvConvert, postList, region);
    }

    private List<UserCsvConvert> getAllRecords() {
        try {
            return new CsvToBeanBuilder<UserCsvConvert>(new FileReader(SOURCE_PATH))
                    .withType(UserCsvConvert.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private List<UserCsvConvert> saveToData(List<UserCsvConvert> users) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(SOURCE_PATH))) {
            StatefulBeanToCsv<UserCsvConvert> writer = new StatefulBeanToCsvBuilder<UserCsvConvert>(csvWriter).build();
            writer.write(users);
            return users;
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private Long getId() {
        try {
            lock.lock();
            if (id == null) {
                id = new AtomicLong(getLastId() + 1L);
            }

            return id.getAndIncrement();
        } finally {
            lock.unlock();
        }
    }

    private Long getLastId() {
        List<UserCsvConvert> users = getAllRecords();

        if (users.size() == 0) {
            return 0L;
        }

        return users.stream()
                .max(Comparator.comparingLong(UserCsvConvert::getId))
                .get()
                .getId();
    }
}