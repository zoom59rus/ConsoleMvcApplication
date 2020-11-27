package com.zoom59rus.javacore.chapter13.view;

import com.zoom59rus.javacore.chapter13.InputFilter;
import com.zoom59rus.javacore.chapter13.controller.UserController;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.dtos.UserDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class UserMenu {
    private static UserMenu userMenu;
    private UserController userController;
    private BufferedReader br;

    private UserMenu() throws IOException {
        userController = new UserController();
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    public static UserMenu getInstance() {
        if (userMenu == null) {
            try {
                userMenu = new UserMenu();
            } catch (IOException e) {
                System.err.print(e.getMessage());
            }
        }

        return userMenu;
    }

    public void showMainMenu() throws IOException {
        System.out.println("Выберите действие:");
        System.out.println();
        System.out.println("[1] Добавить пользователя.");
        System.out.println("[2] Найти пользователя.");
        System.out.println("[3] Изменить пользователя.");
        System.out.println("[4] Удалить пользователя.");
        System.out.println("[0] Выход.");

        switch (matchMenuNumber()) {
            case 1: {
                menuAddUser();
            }
            case 2: {
                searchUserMenu();
            }
            case 3: {
                updateUserMenu();
            }
            case 4: {
                removeUserMenu();
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

    public void menuAddUser() {
        System.out.println("Меню создания пользователя:");
        try {
            System.out.print("Введите имя: ");
            String firstName = br.readLine();
            while (matchName(firstName)) {
                System.out.println("Вы ошиблись в написании имени, попробуйте еще раз.");
                System.out.print("Введите имя: ");
                firstName = br.readLine();
            }

            System.out.print("Введите фамилию: ");
            String lastName = br.readLine();
            while (matchName(lastName)) {
                System.out.println("Вы ошиблись в написании фамилии, попробуйте еще раз.");
                System.out.print("Введите фамилию: ");
                lastName = br.readLine();
            }

            System.out.println("Введите публикацию(и): ");
            List<Post> postList = new ArrayList<>();
            System.out.print("Введите публикацию №" + (postList.size() + 1) + ": ");
            String content;
            while (!(content = br.readLine()).equals("")) {
                postList.add(new Post(null, content, new Date()));
                System.out.print("Введите публикацию №" + (postList.size() + 1) + ": ");
            }

            System.out.print("Введите регион: ");
            String r = br.readLine();
            while (matchRegion(r)) {
                System.out.println("Вы ошиблись в написании региона, попробуйте еще раз.");
                System.out.print("Введите регион: ");
                r = br.readLine();
            }
            Region region = new Region(null, r);

            userController.save(new UserDto(null, firstName, lastName, postList, region));

            showMainMenu();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchUserMenu() throws IOException {
        System.out.println("Меню поиска пользователя.");
        System.out.println("Критерий поиска:");
        System.out.println("[1] Поиск по id.");
        System.out.println("[2] Поиск по Имени.");
        System.out.println("[3] Поиск по Фамилии.");
        System.out.println("[4] Поиск по Имени и Фамилии.");
        System.out.println("[5] Поиск по контенту.");
        System.out.println("[6] Поиск по региону.");
        System.out.println("[7] Возврат в главное меню.");
        System.out.println("[0] Выход из программы.");

        switch (matchMenuNumber()) {
            case 1: {
                System.out.print("Введите id пользователя: ");
                Long id = Long.parseLong(br.readLine());
                System.out.println(userController.getById(id));
                searchUserMenu();
            }
            case 2: {
                System.out.print("Введите Имя пользователя: ");
                String fName = br.readLine();
                System.out.println(userController.getByFirstName(fName));
                searchUserMenu();
            }
            case 3: {
                System.out.print("Введите Фамилию пользователя: ");
                String lName = br.readLine();
                System.out.println(userController.getByLastName(lName));
                searchUserMenu();
            }
            case 4: {
                System.out.print("Введите Имя и Фамилию пользователя: ");
                String fName = br.readLine();
                String lName = br.readLine();
                System.out.println(userController.getByFirstAndLastNames(fName, lName));
                searchUserMenu();
            }
            case 5: {
                System.out.print("Введите Контент пользователя: ");
                String content = br.readLine();
                System.out.println(userController.getByContent(content));
                searchUserMenu();
            }
            case 6: {
                System.out.print("Введите Регион пользователя: ");
                String region = br.readLine();
                System.out.println(userController.getByRegion(region));
                searchUserMenu();
            }
            case 7: {
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

    public void removeUserMenu() throws IOException {
        System.out.println("Меню удаления пользователя.");
        System.out.println("Введите id пользователя для удаления.");
        int id = -1;
        try {
            id = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(id == -1){
            return;
        }

        userController.remove(Long.valueOf(id));
    }

    public void updateUserMenu() {
        
    }


    private int matchMenuNumber(){
        int select = -1;

        try {
            System.out.print("Выберите пункт меню: ");
            String input = br.readLine();

            while (!input.matches("[0-9]")){
                System.out.println("Ошибка ввода, попробуйте еще раз.");
                System.out.print("Выберите пункт меню: ");
                input = br.readLine();
            }

            try {
                select = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка!!! Не могу спарсить число!");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return select;
    }

    private boolean matchName(String name){
        if(name.matches("[A-zА-я]+")){
            return true;
        }

        return false;
    }

    private boolean matchRegion(String region){
        if(region.matches("[A-я\\s]+")){
            return true;
        }

        return false;
    }
}