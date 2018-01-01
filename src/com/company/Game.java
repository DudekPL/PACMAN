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
        this.timeforeat = timeforeat;
        this.timeformove = timeformove;
        this.timeforresp = timeforresp;
    }

    public synchronized int getLives() {return lives;}

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
        }
        setChanged();
        notifyObservers();
    }

    public int getScore() { return score; }

    public synchronized void addPoints(int n){
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

    public synchronized void endGame() {
        EndFlag.ending = true;
    }
}


class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }
    public void pause() {
        model.setPaused(!model.isPaused());
    }
    public synchronized void eat() {
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
    public synchronized void collide(int i, State s){
        if (s == State.CHASING) {
            model.player.controller.collided();
            model.lostLive();
        }
        if (s == State.EATABLE) {
            model.addPoints(100);
            try {
                File soundFile = new File("wav/pacman_eatghost.wav");
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
            model.ghosts.get(i).controller.respawn();
        }
        model.ghosts.get(i).controller.collided();
    }
}

public class Game implements Runnable {
    public GameModel model;
    public GameSimpleView view;
    public GameController controller;
    private long timeformove, timeforresp, timeforeat;

    public Game(long timeformove, long timeforeat, long timeforresp) {
        this.timeforeat = timeforeat;
        this.timeformove = timeformove;
        this.timeforresp = timeforresp;
        init();
    }

    public void init(){
        model = new GameModel(timeformove, timeforeat, timeforresp);
        controller = new GameController(model);
        view = new GameSimpleView(this);
        for (int i = 0; i <model.map.model.getSizex() ; i++)
            for (int j = 0; j < model.map.model.getSizey(); j++) model.map.model.field(i,j).view.Timeformove((int)timeformove);
    }

    @Override
    public void run() {
        EndFlag.start();
        Timer chasingtime = new Timer();
        chasingtime.schedule(new GhostTask(this, State.CHASING),0, (long)(1.05 * timeformove));
        Timer eatingtime = new Timer();
        eatingtime.schedule(new GhostTask(this, State.EATABLE),0, (long)(1.7 * timeformove));
        Timer resptime = new Timer();
        resptime.schedule(new GhostTask(this, State.RESPAWNING),0, timeformove);
        Timer pacmantime = new Timer();
        pacmantime.schedule(new PlayerTask(this, (int)timeformove), 0, timeformove);
        Timer updating = new Timer();
        updating.schedule(new UpdateTask(model), 0, 20);
    }
}

class  UpdateTask extends TimerTask {
    private GameModel gm;

    public UpdateTask(GameModel m) {
        gm = m;
    }

    @Override
    public void run() {
        if (!EndFlag.ending) {
            for (Ghost g: gm.ghosts) {
                g.controller.setActPos();
            }
            gm.player.controller.setActPos();
        }
        if (EndFlag.ending) {
            EndFlag.updating = true;
            cancel();
        }
    }
}

class GhostTask extends TimerTask{
    private Game gm;
    private State state;
    private int i;

    public GhostTask(Game g, State s){
        super();
        gm = g;
        state = s;
    }

    @Override
    public void run(){
        if (!gm.model.isPaused())
            i = 0;
            for (Ghost g: gm.model.ghosts) {
                synchronized (g.model) {
                    if (g.model.getStatus() == state) {
                        if (Math.abs(g.model.getActposx() - gm.model.player.model.getActposx()) < (38) &&
                                Math.abs(g.model.getActposy() - gm.model.player.model.getActposy()) < (38) ){
                            gm.controller.collide(i, g.model.getStatus());
                        }
                        g.controller.move(gm.model.player.model.getPosx(), gm.model.player.model.getPosy());
                    }
                    i +=1;
                }
            }
        if (EndFlag.ending){
            switch (state) {
                case RESPAWNING:
                    EndFlag.resping = true;
                    break;
                case EATABLE:
                    EndFlag.eating = true;
                    break;
                case CHASING:
                    EndFlag.chasing = true;
                    break;
            }
            cancel();
        }
    }
}

class PlayerTask extends TimerTask {
    private Game gm;
    private int delay;

    public PlayerTask(Game g, int delay) {
        super();
        gm = g;
        this.delay = delay;
    }

    @Override
    public void run() {
        if (!gm.model.isPaused()) {
            gm.model.player.controller.move();
            gm.controller.eat();
            if (gm.model.map.model.isEmpty()) {
                javax.swing.Timer t = new javax.swing.Timer(0, event->{
                    gm.model.map.model.init();
                    for (Ghost g : gm.model.ghosts) {
                        g.controller.respawn();
                    }
                    gm.model.player.controller.respawn();
                });
                t.setInitialDelay(delay);
                t.setRepeats(false);
                t.start();
            }
        }
        if (EndFlag.ending){
            EndFlag.pacman = true;
            cancel();
        }
    }
}