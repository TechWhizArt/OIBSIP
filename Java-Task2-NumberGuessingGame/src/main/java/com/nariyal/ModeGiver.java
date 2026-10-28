package com.nariyal;



public class ModeGiver {

    public Mode modeGiver(int ch){
        Mode mode = new Mode();
        mode.setStart(0);
        if(ch == 1){
            mode.setEnd(50);
            mode.setChances(10);
            return mode;
        }else if(ch == 2){
            mode.setEnd(100);
            mode.setChances(8);
            return mode;
        }else if(ch == 3){
            mode.setEnd(200);
            mode.setChances(5);
            return mode;
        }else{
            throw new IllegalArgumentException("Wrong choice of mode");
        }
    }

}
