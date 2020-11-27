package com.zoom59rus.javacore.chapter13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InputFilter {

    public static boolean matchName(String name){
        if(name.matches("[A-zА-я]+")){
            return true;
        }

        return false;
    }

    public static boolean matchEmail(String email){
        if(email.matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")){
            return true;
        }

        return false;
    }

    public static boolean matchRegion(String region){
        if(region.matches("[A-я\\s]+")){
            return true;
        }

        return false;
    }

    public static int matchMenuNumber(){
        int select = -1;

        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            System.out.print("Выберите пункт меню: ");
            String input = br.readLine();

//            while (!input.matches("[0-9]")){
//                System.out.println("Ошибка ввода, попробуйте еще раз.");
//                System.out.print("Выберите пункт меню: ");
//                input = br.readLine();
//            }

            isr.close();
            br.close();
            select = Integer.parseInt(input);


//            try {
//                select = Integer.parseInt(input);
//            } catch (NumberFormatException e) {
//                System.out.println("Ошибка!!! Не могу спарсить число!");
//            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return select;
    }
}
