package com.company;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

class GameModel extends Observable{
    int score;
    int lives;
    Map map;
    Pacman player;
    List<Ghost> ghosts;
    long timeforeat,timeformove,timeforresp;
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
        this.timeforeat = timeforeat;
        this.timeformove = timeformove;
        this.timeforresp = timeforresp;
    }

    public int getLives() {return lives;}

    public void lostLive() {
        lives = lives - 1;
        try {
            File soundFile = new File("wav/pacman_death.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
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
        Inside i = model.map.controller.eat(model.player.model.getPosx(), model.player.model.getPosy(), (int)model.timeformove);
        switch (i) {
            case DOT: model.addPoints(10);
                break;
            case BIGDOT: model.addPoints(10);
                for (Ghost g: model.ghosts) {
                    javax.swing.Timer t = new javax.swing.Timer(0, event->g.controller.makeEatable());
                    t.setInitialDelay((int)model.timeformove);
                    t.setRepeats(false);
                    t.start();
                }
                break;
            case FRUIT: model.addPoints(50);
        }
        if(i != Inside.EMPTY) {
            javax.swing.Timer t = new javax.swing.Timer(0, event->{
                try {
                    File soundFile = new File("wav/pacman_eatfruit.wav");
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            });
            t.setInitialDelay((int)model.timeformove);
            t.setRepeats(false);
            t.start();
        }
    }
}

public class Game implements Runnable {
    public GameModel model;
    public GameSimpleView view;
    public GameController controller;
    private boolean ended = false;
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
        chasingtime.schedule(new GhostTask(this, State.CHASING),0, timeformove);
        Timer eatingtime = new Timer();
        eatingtime.schedule(new GhostTask(this, State.EATABLE),0, (long)(1.3 * timeformove));
        Timer resptime = new Timer();
        resptime.schedule(new GhostTask(this, State.RESPAWNING),0, timeformove);
        Timer pacmantime = new Timer();
        pacmantime.schedule(new PlayerTask(this), 0, timeformove);
    }
}

class GhostTask extends TimerTask{
    private Game gm;
    private State state;

    public GhostTask(Game g, State s){
        super();
        gm = g;
        state = s;
    }

    @Override
    public void run(){
        if (!gm.model.isPaused())
            for (Ghost g: gm.model.ghosts) {
                if (g.model.getStatus() == state) g.controller.move();
            }
    }
}

class PlayerTask extends TimerTask {
    private Game gm;

    public PlayerTask(Game g) {
        super();
        gm = g;
    }

    @Override
    public void run() {
        if (!gm.model.isPaused()) {
            gm.model.player.controller.move();
            gm.controller.eat();
            if (gm.model.map.model.isEmpty()) {
                gm.model.map.model.init();
                for (Ghost g : gm.model.ghosts) {
                    g.controller.respawn();
                }
                gm.model.player.controller.respawn();
            }
        }
    }
}