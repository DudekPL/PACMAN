package com.company;

public class EndFlag {
    public static boolean chasing ;
    public static boolean ending;
    public static boolean eating;
    public static boolean resping;
    public static boolean pacman;
    public static boolean ended;
    public static boolean updating;

    public static void start() {
        chasing = false;
        ending = false;
        eating = false;
        resping = false;
        pacman = false;
        ended = false;
        updating = false;
    }

    public static boolean isEnded(){
        return (chasing && ending && eating && resping && pacman && updating);
    }

    public static int numb() {
        int i = 0;
        if (chasing) i+=i;
        if (ending) i+=i;
        if (eating) i+=i;
        if (resping) i+=i;
        if (pacman) i+=i;
        if (updating) i+=1;
        return i;
    }

    public static void init() {
        chasing = true;
        ending = true;
        eating = true;
        resping = true;
        pacman = true;
        ended = true;
        updating = true;
    }
}
