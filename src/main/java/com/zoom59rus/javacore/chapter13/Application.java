package com.zoom59rus.javacore.chapter13;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import com.zoom59rus.javacore.chapter13.repository.PostRepository;
import com.zoom59rus.javacore.chapter13.repository.UserRepository;
import com.zoom59rus.javacore.chapter13.repository.io.csv.CsvPostRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.io.csv.CsvUserRepositoryImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {
//        UserMenu userMenu = UserMenu.getInstance();
//        userMenu.showMainMenu();

        UserRepository uRepository = CsvUserRepositoryImpl.getInstance();
        PostRepository postRepository = CsvPostRepositoryImpl.getInstance();

        List<Post> posts = new ArrayList<>();
        Post post = new Post(null, "Post 738", new Date());
        Post post1 = new Post(null, "Post 345", new Date());
        Post post2 = new Post(null, "Post 423", new Date());
        Post post3 = new Post(null, "Post 14", new Date());
        posts.add(post);
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);

        User user = new User(null, "Anton", "Nazarov", posts, new Region(null, "Пермский край"));
        User user2 = new User(null, "Zurab", "Matua", posts, new Region(null, "CC"));

        List<User> s = new ArrayList<>();
        s.add(user);
        s.add(user2);

        uRepository.saveAll(s);

    }
}