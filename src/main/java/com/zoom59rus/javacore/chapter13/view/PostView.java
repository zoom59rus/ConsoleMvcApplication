package com.zoom59rus.javacore.chapter13.view;

import com.zoom59rus.javacore.chapter13.controller.PostController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class PostView {
    private final PostController postController;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public PostView() {
        this.postController = new PostController();
    }

    public void getPost(String content) {
        System.out.println(postController.get(content));
    }

    public void search(String content) {
        System.out.println(ANSI_GREEN + "Найден контент(ы): " + ANSI_RESET);
        postController.search(content).forEach((k, v) -> System.out.println(ANSI_GREEN + "\t\t[" + k + "]. " + v + ANSI_RESET));
    }

    public void update() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите контент для редактирования: ");
        String searchPost = sc.nextLine();
        System.out.print("Запись для редактирования: ");
        getPost(searchPost);
        System.out.print("Введите новое значение: ");
        String updatePostContent = sc.nextLine();
        System.out.print(ANSI_RED + "Существующая запись будет заменена на \"" +
                updatePostContent + "\"" +
                " (Y/N): " +
                ANSI_RESET);

        String input = sc.next();
        if (input.toLowerCase().equals("y")) {
            String result = postController.update(searchPost, updatePostContent);
            if (result != null) {
                System.out.println(ANSI_GREEN +
                        "Запись отредактирована на: " + result +
                        ANSI_RESET);
            }
        } else System.out.println("Редактирование отменено пользователем.");

        sc.close();
    }

    public void add() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите публикацию: ");
        String input = sc.nextLine();
        String savedPost = postController.save(input);
        if (savedPost != null) {
            System.out.println(ANSI_GREEN + "Контент: " + savedPost + " сохранен." + ANSI_RESET);
        } else System.err.println("Не удалось сохранить.");
        sc.close();
    }

    public List<String> createPostDialog(Scanner sc) {
        List<String> contentList = new ArrayList<>();

        System.out.println("Введите публикацию(и), или Enter для завершения ввода: ");
        System.out.print("Введите публикацию №" + (contentList.size() + 1) + ": ");
        String content;
        while (!(content = sc.nextLine()).equals("")) {
            contentList.add(content);
            System.out.print("Введите публикацию №" + (contentList.size() + 1) + ": ");
        }
        return contentList;
    }

    public void print(List<String> postList) {
        AtomicInteger count = new AtomicInteger(1);
        postList.forEach(p -> System.out.println(ANSI_GREEN + "\t\t" + count.getAndIncrement() + p + "\n" + ANSI_RESET));
    }
}
