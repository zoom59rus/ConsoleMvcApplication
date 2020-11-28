package com.zoom59rus.javacore.chapter13;

import com.zoom59rus.javacore.chapter13.view.UserMenu;

import java.io.IOException;

public class Application{
    public static void main(String[] args) throws IOException {
        UserMenu userMenu = UserMenu.getInstance();
        userMenu.showMainMenu();
    }
}