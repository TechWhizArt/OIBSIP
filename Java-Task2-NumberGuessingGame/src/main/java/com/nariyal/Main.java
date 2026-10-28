package com.nariyal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;



public class Main {


    public static void main(String[] args) {


        Instructions.welcomeMsg();  // static method
        boolean replay = true;
        int gamecount = 0;
        List<Integer> list = new ArrayList<>();

        while(replay) {

            Instructions.modeChoice();

            Scanner sc = new Scanner(System.in);

            ModeGiver giver = new ModeGiver();
            Mode mode = giver.modeGiver(sc.nextInt());


            Random random = new Random();
            int num = generateRandomNumber(mode.getStart(), mode.getEnd());

            boolean res = false;
            int chances = 0;
            while (res == false) {
                System.out.println("Take a guess:");
                int guessedNum = sc.nextInt();
                res = Game.guessNumber(guessedNum, num);
                chances++;
                if (chances >= mode.getChances()) {
                    System.out.println("Max chances reached!!!");
                    System.out.println("You lose!!!");
                    break;
                }
            }
            if(res == true){
                list.add(chances);
            }else{
                list.add(-1);
            }
            sc.nextLine();
            System.out.println("Wanna replay?\n" + "y/n:");

            if(sc.nextLine().equalsIgnoreCase("y")){
                replay = true;
            }else{
                replay = false;
            }
            gamecount++;
        }

        Instructions.ResultScreen(list, gamecount);
    }
    public static int generateRandomNumber(int min, int max) {
        return new Random().nextInt(min, max + 1);
    }
}