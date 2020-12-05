package com.zoom59rus.javacore.chapter13.view;

import com.zoom59rus.javacore.chapter13.controller.UserController;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserView {
    private final UserController userController;
    private final PostView postView;
    private final RegionView regionView;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public UserView() {
        this.userController = new UserController();
        this.postView = new PostView();
        this.regionView = new RegionView();
    }

    public void createUserDialog() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введите имя пользователя: ");
        String firstName = matchName();

        System.out.print("Введите фамилию: ");
        String lastName = matchName();

        List<String> contents = postView.createPostDialog(sc);
        String region = regionView.createRegionDialog(sc);

        System.out.println("Создан пользователь:");
        printUser(firstName, lastName, contents, region);
        System.out.print(ANSI_RED + "Сохранить пользователя (Y/N)?: " + ANSI_RESET);

        String i = sc.next();
        if ("y".equals(i.toLowerCase())) {
            boolean saved = userController.createUser(firstName, lastName, contents, region);
            if (saved) {
                System.out.println(ANSI_GREEN + "Пользователь сохранен." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Пользователь не сохранен." + ANSI_RESET);
            }
        }
        sleep();
    }

    public void searchUserDialog(int select) {
        switch (select) {
            case 1: {
                System.out.print("Введите имя пользователя: ");
                String firstName = matchName();
                List<User> findUsers = userController.getUsersByFirstName(firstName);
                findUsers.stream()
                        .forEach(u -> printUser(u.getFirstName(),
                                u.getLastName(),
                                u.getPosts().stream().map(Post::getContent).collect(Collectors.toList()),
                                u.getRegion().getName()));
            }
            case 2: {
                System.out.print("Введите фамилию пользователя: ");
                String lastName = matchName();
                List<User> findUsers = userController.getUsersByLastName(lastName);
                findUsers.stream()
                        .forEach(u -> printUser(u.getFirstName(),
                                u.getLastName(),
                                u.getPosts().stream().map(Post::getContent).collect(Collectors.toList()),
                                u.getRegion().getName()));
            }
            case 3: {
                System.out.println("Введите имя и фамилию пользователя: ");
                System.out.print("Введите имя: ");
                String firstName = matchName();
                System.out.print("Введите фамилию: ");
                String lastName = matchName();
                List<User> findUsers = userController.getUsersByFirstAndLastName(firstName, lastName);
                findUsers.stream()
                        .forEach(u -> printUser(u.getFirstName(),
                                u.getLastName(),
                                u.getPosts().stream().map(Post::getContent).collect(Collectors.toList()),
                                u.getRegion().getName()));
            }
            case 4: {
                System.out.print("Введите публикацию пользователя: ");
                String content = matchName();
                List<User> findUsers = userController.getUsersByPost(content);
                findUsers.stream()
                        .forEach(u -> printUser(u.getFirstName(),
                                u.getLastName(),
                                u.getPosts().stream().map(Post::getContent).collect(Collectors.toList()),
                                u.getRegion().getName()));
            }
            case 5: {
                System.out.print("Введите регион пользователя: ");
                String region = matchName();
                List<User> findUsers = userController.getUsersByRegion(region);
                findUsers.stream()
                        .forEach(u -> printUser(u.getFirstName(),
                                u.getLastName(),
                                u.getPosts().stream().map(Post::getContent).collect(Collectors.toList()),
                                u.getRegion().getName()));
            }
        }
    }

    public void removeUserDialog() {
        System.out.print("Введите имя пользователя для удаления: ");
        Scanner sc = new Scanner(System.in);
        String firstName = matchName();
        User findUser = userController.getByFirstName(firstName);
        printUser(findUser);
        System.out.print(ANSI_RED + "Пользователь будет удален, подтвердите (Y/N): " + ANSI_RESET);
        String input = sc.next();
        if ("y".equals(input.toLowerCase())) {
            if (userController.remove(findUser)) {
                System.out.println(ANSI_GREEN + "Пользователь удален." + ANSI_RESET);
            } else System.err.println("Невозможно удалить пользователя \"" +
                    findUser.getFirstName() + findUser.getLastName() +
                    "\"" + ", или пользователь не существует.");
        }
    }

    public void updateUserDialog() {
        System.out.print("Введите имя пользователя для редактирования: ");
        String firstName = matchName();

        User updateUser = userController.getByFirstName(firstName);
        if (updateUser == null) {
            System.out.println(ANSI_GREEN + "Пользователь с именем:" + firstName + " не найден." + ANSI_RESET);
            return;
        }

        System.out.println("Редактируемый пользователь:");
        printUser(updateUser);

        Scanner sc = new Scanner(System.in);
        System.out.println("Ведите новые записи или нажмите Enter для пропуска:");
        System.out.print("Введите имя: ");
        String input = sc.nextLine();
        if(!input.equals("")){
            updateUser.setFirstName(input);
        }
        System.out.print("Введите фамилию: ");
        input = sc.nextLine();
        if(!input.equals("")){
            updateUser.setLastName(input);
        }
        System.out.print("Введите регион: ");
        input = sc.nextLine();
        if(!input.equals("")){
            updateUser.setRegion(new Region(null, input));
        }
        System.out.println("Отредактируйте публикации, или нажмите Enter для пропуска: ");
        List<Post> postList = new ArrayList<>();
        for (Post post : updateUser.getPosts()) {
            System.out.println(ANSI_GREEN + post.getContent() + ANSI_RESET);
            System.out.print("Новая запись: ");
            String content = sc.nextLine();
            if(!content.equals("")){
                postList.add(new Post(null, content, new Date()));
            }else postList.add(post);

        }
        updateUser.setPosts(postList);
        printUser(updateUser);
        System.out.print("Сохранить изменения пользователя? Y/N: ");
        String s = sc.next();
        if ("y".equals(s.toLowerCase())) {
            userController.update(updateUser);
            System.out.println(ANSI_GREEN + "Пользователь сохранен." + ANSI_RESET);
        }else System.out.println("Обновление отменено пользователем.");
    }

    private String matchName() {
        Scanner sc = new Scanner(System.in);
        String firstName = sc.nextLine();
        while (!firstName.matches("[A-zА-я]+")) {
            System.err.println("Вы ошиблись в написании имени, попробуйте еще раз.");
            System.out.print("Введите имя: ");
            firstName = sc.nextLine();
        }

        return firstName;
    }

    private void printUser(String firstName, String lastName, List<String> contents, String region) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        sb
                .append("Имя: " + "\"" + firstName + "\"" + "\n")
                .append("Фамилия: " + "\"" + lastName + "\"" + "\n")
                .append("Публикация(и):\n");
        for (String content : contents) {
            sb.append("\t\t" + (count++) + ". " + "\"" + content + "\"" + "\n");
        }
        sb.append("Регион пользователя: " + "\"" + region + "\"");
        System.out.println(ANSI_GREEN + sb.toString() + ANSI_RESET);
    }

    private void printUser(User user) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        sb
                .append("Имя: " + "\"" + user.getFirstName() + "\"" + "\n")
                .append("Фамилия: " + "\"" + user.getLastName() + "\"" + "\n")
                .append("Публикация(и):\n");
        for (Post content : user.getPosts()) {
            sb.append("\t\t" + (count++) + ". " + "\"" + content.getContent() + "\"" + "\n");
        }
        sb.append("Регион пользователя: " + "\"" + user.getRegion().getName() + "\"");
        System.out.println(ANSI_GREEN + sb.toString() + ANSI_RESET);
    }

    private void sleep() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Для продолжения нажмите любую клавишу...");
        sc.nextLine();
    }
}
