package com.zoom59rus.javacore.chapter13.view;

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

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private UserMenu(){
        userController = new UserController();
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    public static UserMenu getInstance() {
        if (userMenu == null) {
            userMenu = new UserMenu();
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
            while (!matchName(firstName)) {
                System.err.println("Вы ошиблись в написании имени, попробуйте еще раз.");
                System.out.print("Введите имя: ");
                firstName = br.readLine();
            }

            System.out.print("Введите фамилию: ");
            String lastName = br.readLine();
            while (!matchName(lastName)) {
                System.err.println("Вы ошиблись в написании фамилии, попробуйте еще раз.");
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
            while (!matchRegion(r)) {
                System.err.print("Вы ошиблись в написании региона, попробуйте еще раз.");
                System.out.print("Введите регион: ");
                r = br.readLine();
            }
            Region region = new Region(null, r);
            UserDto userDto = new UserDto(null, firstName, lastName, postList, region);
            System.out.println(ANSI_GREEN + userDto + ANSI_RESET);
            System.out.print("Сохранить пользователя? Y/N: ");
            String s = br.readLine();
            if("y".equals(s.toLowerCase())){
                userController.save(userDto);
                System.out.println("Пользователь сохранен.");
            }

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
                String idString = br.readLine();
                while (!matchId(idString)){
                    idString = br.readLine();
                }
                Long id = Long.parseLong(idString);
                UserDto result = userController.getById(id);
                if(result != null) {
                    System.out.println(ANSI_GREEN + result + ANSI_RESET);
                } else System.out.println(ANSI_GREEN + "Пользователь с id:" + id + " не найден" + ANSI_RESET);

                sleep();
                searchUserMenu();
            }
            case 2: {
                System.out.print("Введите Имя пользователя: ");
                String fName = getInputName();
                UserDto result = userController.getByFirstName(fName);
                if(result != null){
                    System.out.println(ANSI_GREEN + result + ANSI_RESET);
                }else System.out.println(ANSI_GREEN + "Пользователь с именем:" + fName + " не найден" + ANSI_RESET);

                sleep();
                searchUserMenu();
            }
            case 3: {
                System.out.print("Введите Фамилию пользователя: ");
                String lName = getInputName();
                UserDto result = userController.getByLastName(lName);
                if(result != null){
                    System.out.println(ANSI_GREEN + result + ANSI_RESET);
                }else System.out.println(ANSI_GREEN + "Пользователь с фамилией:" + lName + " не найден" + ANSI_RESET);

                sleep();
                searchUserMenu();
            }
            case 4: {
                System.out.println("Введите Имя и Фамилию пользователя:");
                System.out.print("Введите имя: ");
                String fName = getInputName();
                System.out.print("Введите фамилию: ");
                String lName = getInputName();
                UserDto result = userController.getByFirstAndLastNames(fName, lName);
                if(result != null){
                    System.out.println(ANSI_GREEN + result + ANSI_RESET);
                }else System.out.println(ANSI_GREEN + "Пользователь " + fName + " " + lName + " не найден" + ANSI_RESET);

                sleep();
                searchUserMenu();
            }
            case 5: {
                System.out.print("Введите Контент пользователя: ");
                String content = br.readLine();
                UserDto result = userController.getByContent(content);
                if(result != null){
                    System.out.println(ANSI_GREEN + result + ANSI_RESET);
                }else System.out.println(ANSI_GREEN + "Пользователь с контентом " + content + " не найден" + ANSI_RESET);

                sleep();
                searchUserMenu();
            }
            case 6: {
                System.out.print("Введите Регион пользователя: ");
                String region = getInputRegion();
                List<UserDto> result = userController.getUsersByRegion(region);
                if(result != null){
                    result.forEach(r -> System.out.println(ANSI_GREEN + r + ANSI_RESET));
                }else System.out.println(ANSI_GREEN + "Пользователь с регионом " + region + " не найден" + ANSI_RESET);

                sleep();
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
                searchUserMenu();
            }
        }
    }

    public void removeUserMenu() throws IOException {
        System.out.print("Введите id пользователя: ");
        String idString = br.readLine();
        while (!matchId(idString)){
            idString = br.readLine();
        }
        Long id = Long.parseLong(idString);
        UserDto userDto = userController.getById(id);
        if(userDto == null){
            System.out.println(ANSI_GREEN + "Пользователь с id:" + id + " не найден." + ANSI_RESET);
            showMainMenu();
        }
        System.out.println(ANSI_GREEN + userDto + ANSI_RESET);
        System.out.print(ANSI_RED + "Пользователь будет удален, подтвердите (Y/N): " + ANSI_RESET);
        String input = br.readLine();
        if("y".equals(input.toLowerCase())){
            if(userController.remove(id)) {
                System.out.println(ANSI_GREEN + "Пользователь удален." + ANSI_RESET);
            }else System.err.println("Невозможно удалить пользователя с id:" + id + ", или пользователь не существует.");
        }

        showMainMenu();
    }

    public void updateUserMenu() throws IOException {
        System.out.println("Меню редактирования пользователя.");
        System.out.print("Введите id пользователя для редактирования: ");
        String idString = br.readLine();
        while (!matchId(idString)){
            idString = br.readLine();
        }
        Long id = Long.parseLong(idString);

        UserDto updateUser = userController.getById(id);
        if(updateUser == null){
            System.out.println(ANSI_GREEN + "Пользователь с id:" + id + " не найден." + ANSI_RESET);
            showMainMenu();
        }

        System.out.println("Редактируемый пользователь:");
        System.out.println(ANSI_GREEN + updateUser + ANSI_RESET);

        try {
            System.out.print("Введите имя: ");
            String firstName = br.readLine();
            while (!matchName(firstName)) {
                System.err.print("Вы ошиблись в написании имени, попробуйте еще раз.");
                System.out.print("Введите имя: ");
                firstName = br.readLine();
            }

            System.out.print("Введите фамилию: ");
            String lastName = br.readLine();
            while (!matchName(lastName)) {
                System.err.print("Вы ошиблись в написании фамилии, попробуйте еще раз.");
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
            while (!matchRegion(r)) {
                System.err.print("Вы ошиблись в написании региона, попробуйте еще раз.");
                System.out.print("Введите регион: ");
                r = br.readLine();
            }
            Region region = new Region(null, r);
            UserDto userDto = new UserDto(id, firstName, lastName, postList, region);
            System.out.println(ANSI_GREEN + userDto + ANSI_RESET);

            System.out.print("Сохранить изменения пользователя? Y/N: ");
            String s = br.readLine();
            if("y".equals(s.toLowerCase())){
                userController.update(userDto);
                System.out.println("Пользователь сохранен.");
            }


            showMainMenu();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

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

    private boolean matchId(String idString){
        try{
            Long.parseLong(idString);
            return true;
        }catch (NumberFormatException e){
            System.err.println("Неправильный id, попробуйте еще раз.");
            System.out.print("Введите id пользователя для редактирования: ");
            return false;
        }
    }

    private String getInputName(){
        try {
            String name = br.readLine();
            while (!matchName(name)){
                System.err.println("Вы ошиблись в написании, попробуйте еще раз.");
                name = br.readLine();
            }
            return name;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getInputRegion(){
        try {
            String region = br.readLine();
            while (!matchRegion(region)){
                System.err.println("Вы ошиблись в написании, попробуйте еще раз.");
                System.out.print("Введите регион пользователя: ");
                region = br.readLine();
            }
            return region;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sleep(){
        System.out.println("Для продолжения нажмите любую клавишу...");
        try {
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}