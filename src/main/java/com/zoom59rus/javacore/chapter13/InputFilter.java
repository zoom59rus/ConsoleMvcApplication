package main.java.com.zoom59rus.javacore.chapter13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try{
            System.out.print("Выберите пункт меню: ");
            String input = br.readLine();

            while (!input.matches("[0-9]")){
                System.out.println("Ошибка ввода, попробуйте еще раз.");
                System.out.print("Выберите пункт меню: ");
                input = br.readLine();
            }

            try {
                select = Integer.parseInt(input);
                return select;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка!!! Не могу спарсить число!");
            }

            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally {
            if(select == -1){
                matchMenuNumber();
            }
        }

        return select;
    }
}
