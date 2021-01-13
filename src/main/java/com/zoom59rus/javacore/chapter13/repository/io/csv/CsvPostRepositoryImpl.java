package com.zoom59rus.javacore.chapter13.repository.io.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.repository.PostRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CsvPostRepositoryImpl implements PostRepository {
    private static CsvPostRepositoryImpl INSTANCE;
    private static final String SOURCE_PATH = "src/main/resources/files/csv/post.csv";
    private volatile AtomicLong id;
    private Lock lock;

    private CsvPostRepositoryImpl() {
        this.lock = new ReentrantLock();
    }

    public static CsvPostRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (CsvPostRepositoryImpl.class) {
                INSTANCE = new CsvPostRepositoryImpl();
            }
        }
        return INSTANCE;
    }

    @Override
    public List<Post> getPostListByIds(List<Long> postIds) {
        return getAll().stream()
                .filter(p -> postIds.contains(p.getId()))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<Post> get(String content) {
        return getAll().stream()
                .filter(p -> p.getContent().equals(content))
                .findFirst();
    }

    @Override
    public Post save(Post post) throws IOException {
        List<Post> posts = getAll();
        if (posts.contains(post)) {
            return posts.stream()
                    .filter(p -> p.getContent().equals(post.getContent()))
                    .findFirst()
                    .get();
        }

        post.setId(getId());
        posts.add(post);
        saveToData(posts);

        return post;
    }

    @Override
    public List<Post> saveAll(List<Post> lists) throws IOException {
        List<Post> posts = getAll();

        posts.addAll(lists.stream()
                .peek(p -> p.setId(getId()))
                .collect(Collectors.toList()));

        return saveToData(posts);
    }

    @Override
    public Optional<Post> get(Long id) {
        return getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Post> getAll() {
        try {
            return new CsvToBeanBuilder<Post>(new FileReader(SOURCE_PATH))
                    .withType(Post.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    public boolean remove(Long id) throws IOException {
        List<Post> posts = getAll();

        if (posts.removeIf(p -> p.getId().equals(id))) {
            saveToData(posts);
            return true;
        }

        return false;
    }

    @Override
    public Post update(Post post) throws IOException {
        List<Post> posts = getAll();

        if (posts.removeIf(p -> p.getId().equals(post.getId()))) {
            post.setUpdated(new Date());
            posts.add(post);
            saveToData(posts);
            return post;
        }

        return null;
    }

    private Long getId() {
        lock.lock();
        try {
            if (id == null) {
                id = new AtomicLong(lastId() + 1L);
            }

            return id.getAndIncrement();
        } finally {
            lock.unlock();
        }
    }

    private Long lastId() {
        List<Post> allPostsRecorded = getAll();

        if (allPostsRecorded.size() == 0) {
            return 0L;
        }

        return allPostsRecorded.stream()
                .max(Comparator.comparingLong(Post::getId))
                .get()
                .getId();
    }

    private List<Post> saveToData(List<Post> posts) throws IOException {
        try (CSVWriter csw = new CSVWriter(new FileWriter(SOURCE_PATH))) {
            StatefulBeanToCsv<Post> writer = new StatefulBeanToCsvBuilder<Post>(csw).build();
            writer.write(posts);

            return posts;
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}