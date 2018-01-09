package com.company.model;

import com.company.util.EndFlag;
import com.company.util.SavingScore;
import com.company.wrapper.Ghost;
import com.company.wrapper.Map;
import com.company.wrapper.Pacman;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Observable;


public class GameModel extends Observable {
    int score;
    int lives;
    public Map map;
    public Pacman player;
    public List<Ghost> ghosts;
    public final long timeforeat,timeformove,timeforresp;
    private boolean paused;

    public GameModel(long timeformove, long  timeforeat, long timeforresp) {
        score = 0;
        lives = 3;
        paused = false;
        map = new Map();
        ghosts = new ArrayList<>();
        ghosts.add(new Ghost(map, 7, 4, Color.RED, map.view.FIELD_SIZE, timeformove, timeforeat, timeforresp));
        ghosts.add(new Ghost(map, 9, 4, Color.YELLOW, map.view.FIELD_SIZE, timeformove, timeforeat, timeforresp));
        ghosts.add(new Ghost(map, 9, 4, Color.BLUE, map.view.FIELD_SIZE, timeformove, timeforeat, timeforresp));
        player = new Pacman(map, 8, 6, map.view.FIELD_SIZE, timeformove);
        this.timeforeat = timeforeat;
        this.timeformove = timeformove;
        this.timeforresp = timeforresp;
    }

    public synchronized int getLives() {return lives;}

    public void lostLive() {
        lives = lives - 1;
        for (Ghost g:ghosts) {
            g.controller.respawn();
        }
        if (lives < 1) {
            endGame();
        }
        setChanged();
        notifyObservers(Boolean.TRUE);
    }

    public int getScore() { return score; }

    public synchronized void addPoints(int n){
        score = score + n;
        if (score>=1000000) score = 999999;
        setChanged();
        notifyObservers(Boolean.FALSE);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public synchronized void endGame() {
        SavingScore.upload(score);
        EndFlag.ending = true;
    }
}