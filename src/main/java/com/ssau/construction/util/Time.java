package com.ssau.construction.util;

public class Time {
    private int seconds;
    private int minutes;

    public void inc(){
        if (seconds==59){
            minutes++;
            seconds=0;
        }
        else seconds++;
    }

    @Override
    public String toString(){
        return String.format("%02d:%02d", minutes, seconds );
    }
}
