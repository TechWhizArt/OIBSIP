package com.nariyal;

import java.util.ArrayList;
import java.util.List;

public class Instructions {

    public static void welcomeMsg(){
        System.out.println("Hello and welcome! to the number guessing game");
        System.out.println("Im thinking of a number, you guess it ok?");

    }
    public static void modeChoice() {
        System.out.println("Lets first choose a level:\n" +
                "Press \"1\" for Easy\n" +
                "Press \"2\" for medium\n" +
                "Press \"3\" for hard");
    }
    public static void ResultScreen(List<Integer> list,int gamecount) {

        int won = 0;
        System.out.println("Result Screen:");

        for(int i = 0; i<list.size(); i++){
            if(list.get(i) == -1){
                System.out.println("Round " + (i+1) + " lost");
            }else {
                won++;
                System.out.println("Round " + (i+1) + " won in " + list.get(i) + " chances ");
            }
        }
        System.out.println("Player played " + gamecount + " matches, and won " +won+ " matches.\n");
    }
}
