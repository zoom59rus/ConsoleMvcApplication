package com.zoom59rus.javacore.chapter13.view;

import com.zoom59rus.javacore.chapter13.controller.RegionController;

import java.util.Scanner;

public class RegionView {
    private final RegionController regionController;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public RegionView() {
        this.regionController = new RegionController();
    }

    public void getRegion(String name) {
        System.out.println(regionController.get(name).getName());
    }

    public void search(String name) {
        System.out.println(ANSI_GREEN + "Найден регион(ы): " + ANSI_RESET);
        regionController.search(name).forEach((k, v) -> System.out.println(ANSI_GREEN + "\t\t[" + k + "]. " + v + ANSI_RESET));
    }

    public void update() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите запись для редактирования: ");
        String searchName = sc.nextLine();
        System.out.print("Запись для редактирования: ");
        getRegion(searchName);
        System.out.print("Введите новое значение: ");
        String updateRegionName = sc.nextLine();
        System.out.print(ANSI_RED + "Существующая запись будет заменена на \"" +
                updateRegionName + "\"" +
                " (Y/N): " +
                ANSI_RESET);

        String input = sc.next();
        if (input.toLowerCase().equals("y")) {
            Object result = regionController.update(searchName, updateRegionName);
            if (result != null) {
                System.out.println(ANSI_GREEN +
                        "Запись отредактирована на: " + regionController.get(updateRegionName).getName() +
                        ANSI_RESET);

            }
        } else System.out.println("Редактирование отменено пользователем.");

        sc.close();
    }

    public void add() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите регион: ");
        String input = sc.nextLine();
        String savedRegion = regionController.save(input);
        if (savedRegion != null) {
            System.out.println(ANSI_GREEN + "Регион: " + savedRegion + " сохранен." + ANSI_RESET);
        } else System.err.println("Не удалось сохранить.");
        sc.close();
    }

    public String createRegionDialog(Scanner sc) {
        System.out.print("Введите регион: ");
        String region = sc.nextLine();
        while (!matchRegion(region)) {
            System.err.print("Вы ошиблись в написании региона, попробуйте еще раз.");
            System.out.print("Введите регион: ");
            region = sc.nextLine();
        }
        return region;
    }

    public void printRegion(String region) {
        System.out.println(ANSI_GREEN + region + ANSI_RESET);
    }

    private boolean matchRegion(String region) {
        if (region.matches("[A-я\\s]+")) {
            return true;
        }

        return false;
    }
}