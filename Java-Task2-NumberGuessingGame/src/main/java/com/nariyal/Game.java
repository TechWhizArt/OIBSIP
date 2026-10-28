package com.nariyal;

public class Game {





    public static boolean guessNumber(int guess, int num){

        if(guess<0 || guess>200){
            System.out.println("Guessed number does not lie in the range");
        }

        if(guess>num){
            if((guess - num) < 5){
                System.out.println("Slight High!");
            }else{
                System.out.println("Too High!");
            }
            return false;
        }else if(guess<num){
            if((num - guess) < 5){
                System.out.println("Slight Low!");
            }else{
                System.out.println("Too Low!");
            }
            return false;
        }else{
            System.out.println("Guessed number is correct:" + num);
            return true;
        }


    }



}
