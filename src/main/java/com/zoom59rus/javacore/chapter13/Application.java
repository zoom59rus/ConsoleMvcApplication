package com.zoom59rus.javacore.chapter13;
import com.zoom59rus.javacore.chapter13.view.UserMenu;


public class Application{
    public static void main(String[] args){
        UserMenu userMenu = UserMenu.getInstance();
        userMenu.showMainMenu();

    }
}