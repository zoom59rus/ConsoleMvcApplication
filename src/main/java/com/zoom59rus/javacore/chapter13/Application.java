package main.java.com.zoom59rus.javacore.chapter13;

import main.java.com.zoom59rus.javacore.chapter13.controller.UserController;

import java.io.IOException;

public class Application{
    public static void main(String[] args) {
        try {
            UserController userController = new UserController();
            System.out.println(userController.getAllRegions());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}