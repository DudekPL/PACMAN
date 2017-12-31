package com.company;

import java.awt.*;
import java.util.*;
import java.util.List;

class GameModel extends Observable{
    int score;
    int lives;
    Map map;
    Pacman player;
    List<Ghost> ghosts;
    private boolean paused;

    public GameModel(long timeformove, long  timeforeat, long timeforresp) {
        score = 0;
        lives = 3;
        paused = false;
        map = new Map();
        ghosts = new ArrayList<>();
        ghosts.add(new Ghost(map, 7, 4, Color.RED, map.view.FIELD_SIZE, timeformove, timeforeat, timeforresp));
        player = new Pacman(map, 8, 6, map.view.FIELD_SIZE, timeformove);
        map.model.field(8, 6).model.setInside(Inside.EMPTY);
    }

    public int getLives() {return lives;}

    public void lostLive() {
        lives = lives - 1;
        if (lives < 1) {
            endGame();
            return;
        }
        setChanged();
        notifyObservers();
    }

    public int getScore() { return score; }

    public void addPoints(int n){
        score = score + n;
        if (score>=1000000) score = 999999;
        setChanged();
        notifyObservers();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void endGame() {}
}


class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }
    public void pause() {
        model.setPaused(!model.isPaused());
    }
    public void eat() {
        model.map
    }
}

public class Game implements Runnable {
    public GameModel model;
    public GameSimpleView view;
    public GameController controller;
    private long timeformove, timeforresp, timeforeat;

    public Game(long timeformove, long timeforeat, long timeforresp) {
        model = new GameModel(timeformove, timeforeat, timeforresp);
        controller = new GameController(model);
        view = new GameSimpleView(this);
        this.timeforeat = timeforeat;
        this.timeformove = timeformove;
        this.timeforresp = timeforresp;
    }

    @Override
    public void run() {
        Timer chasingtime = new Timer();
        chasingtime.schedule(new GhostTask(model, State.CHASING),0, timeformove);
        Timer eatingtime = new Timer();
        eatingtime.schedule(new GhostTask(model, State.EATABLE),0, (long)(1.3 * timeformove));
        Timer resptime = new Timer();
        resptime.schedule(new GhostTask(model, State.RESPAWNING),0, timeformove);
        Timer pacmantime = new Timer();
        pacmantime.schedule(new PlayerTask(model), 0, timeformove);

    }
}

class GhostTask extends TimerTask{
    private GameModel gm;
    private State state;

    @Override
    public void run(){
        for (Ghost g: gm.ghosts) {
            if (g.model.getStatus() == state) g.controller.move();
        }
    }

    public GhostTask(GameModel g, State s){
        super();
        gm = g;
        state = s;
    }
}

class PlayerTask extends TimerTask {
    private GameModel gm;

    @Override
    public void run() {
        gm.player.controller.move();

    }

    public PlayerTask(GameModel g) {
        super();
        gm = g;
    }
}