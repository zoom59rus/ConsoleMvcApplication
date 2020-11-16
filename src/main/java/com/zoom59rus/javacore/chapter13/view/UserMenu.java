package main.java.com.zoom59rus.javacore.chapter13.view;

import main.java.com.zoom59rus.javacore.chapter13.InputFilter;
import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;
import main.java.com.zoom59rus.javacore.chapter13.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class UserMenu {
    private static UserMenu userMenu;

    private UserMenu() {
    }

    public static UserMenu getInstance() {
        if (userMenu == null) {
            userMenu = new UserMenu();
        }

        return userMenu;
    }

    public void showMainMenu() {
        System.out.println("Выберите действие:");
        System.out.println();
        System.out.println("[1] Добавить пользователя.");
        System.out.println("[2] Найти пользователя.");
        System.out.println("[3] Изменить пользователя.");
        System.out.println("[4] Удалить пользователя.");
        System.out.println("[0] Выход.");

        switch (InputFilter.matchMenuNumber()) {
            case 1: {
                addUserMenu();
            }
            case 2: {
                searchUserMenu();
            }
            case 3: {

            }
            case 4: {

            }
            case 0: {
                System.exit(0);
            }
            default: {
                System.out.println("Выбран не существующий пункт меню.");
                showMainMenu();
            }
        }
    }

    public void addUserMenu() {
        System.out.println("Меню создания пользователя:");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Введите имя: ");
            String firstName = br.readLine();
            while (!InputFilter.matchName(firstName)) {
                System.out.println("Вы ошиблись в написании имени, попробуйте еще раз.");
                System.out.print("Введите имя: ");
                firstName = br.readLine();
            }

            System.out.print("Введите фамилию: ");
            String lastName = br.readLine();
            while (!InputFilter.matchName(lastName)) {
                System.out.println("Вы ошиблись в написании фамилии, попробуйте еще раз.");
                System.out.print("Введите фамилию: ");
                lastName = br.readLine();
            }

            System.out.println("Введите электронную почту(ы): ");
            List<String> postList = new ArrayList<>();
            System.out.print("Введите почту №" + (postList.size() + 1) + ": ");
            String post;
            while (!(post = br.readLine()).equals("")) {
                if (!InputFilter.matchEmail(post)) {
                    System.out.println("Вы ошиблись в написании электронной почты, попробуйте еще раз.");
                    System.out.print("Введите почту №" + (postList.size() + 1) + ": ");
                    continue;
                }
                postList.add(post);
                System.out.print("Введите почту №" + (postList.size() + 1) + ": ");
            }

            System.out.print("Введите регион: ");
            String region = br.readLine();
            while (!InputFilter.matchRegion(region)) {
                System.out.println("Вы ошиблись в написании региона, попробуйте еще раз.");
                System.out.print("Введите регион: ");
                region = br.readLine();
            }

            showMainMenu();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchUserMenu() {
        System.out.println("Меню поиска пользователя.");
        System.out.println("Критерий поиска:");
        System.out.println("[1] Поиск по id.");
        System.out.println("[2] Поиск по Имени.");
        System.out.println("[3] Поиск по Фамилии.");
        System.out.println("[4] Поиск по email.");
        System.out.println("[5] Поиск по региону.");
        System.out.println("[6] Возврат в главное меню.");
        System.out.println("[0] Выход из программы.");

        switch (InputFilter.matchMenuNumber()) {
            case 1: {
            }
            case 2: {
            }
            case 3: {
            }
            case 4: {
            }
            case 5: {
            }
            case 6: {
                showMainMenu();
            }
            case 0: {
                System.exit(0);
            }
            default: {
                System.out.println("Выбран не существующий пункт меню.");
                showMainMenu();
            }
        }
    }

    public void removeUserMenu(){
        System.out.println("Меню удаления пользователя.");
        System.out.println("Введите id пользователя для удаления.");

        int removeUserId = InputFilter.matchMenuNumber();
    }
}