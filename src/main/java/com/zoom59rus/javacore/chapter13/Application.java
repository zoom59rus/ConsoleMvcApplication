package com.zoom59rus.javacore.chapter13;


import com.zoom59rus.javacore.chapter13.controller.PostController;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.view.UserMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application{
    public static void main(String[] args) throws IOException {
        UserMenu userMenu = UserMenu.getInstance();
        userMenu.showMainMenu();





//        PostController postController = new PostController();
////
//        List<Post> postList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            postList.add(new Post(null, "Post #" + i, new Date()));
//        }
//
//        try {
//            postController.saveAll(postList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            postController.save(new Post(null, "Post ===--", new Date()));
//            postController.save(new Post(null, "Post ===--", new Date()));
//            postController.save(new Post(null, "Post ===--", new Date()));
//            postController.save(new Post(null, "Post ===--", new Date()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            System.out.println(postController.getOne(9L));
//            postController.remove(9L);
//            System.out.println(postController.getOne(9L));
//            System.out.println(postController.getFirstContent("Post #15"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}