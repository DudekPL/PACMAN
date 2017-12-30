package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

class GameModel extends Observable{
    int score;
    int lives;
    private boolean paused;
    Map map;
    Pacman player;
    List<Ghost> ghosts;

    public GameModel() {
        score = 0;
        lives = 3;
        paused = false;
        map = new Map();
        ghosts = new ArrayList<>();
        ghosts.add(new Ghost(map, 7, 4, Color.RED, map.view.FIELD_SIZE, 800));
        player = new Pacman(map, 8, 6, map.view.FIELD_SIZE, 800);
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

class GameSimpleView extends JDesktopPane{
    private GameModel model;

    public GameSimpleView(Game g) {
        GameModel m=g.model;
        setLayout(new FlowLayout());
        model = m;
        setDesktopManager(new DefaultDesktopManager());
        add(model.map.view);
        moveToBack(model.map.view);
        for (Ghost gh: model.ghosts) {
            add(gh.view);
            gh.view.display();
            moveToFront(gh.view);
        }
        add(model.player.view);
        model.player.view.display();
        moveToFront(model.player.view);
        model.map.view.setLocation(0, 0);
        setOpaque(false);
        LifeIcon lico = new LifeIcon(6*model.map.view.FIELD_SIZE, 7*model.map.view.FIELD_SIZE, model.lives, model.map.view.FIELD_SIZE, 8);
        add(lico);
        lico.setLocation(6*model.map.view.FIELD_SIZE, 7*model.map.view.FIELD_SIZE);
        moveToFront(lico);
        m.addObserver(lico);
        ScoreField sf = new ScoreField(model, 6, 1);
        add(sf);
        moveToFront(sf);
        m.addObserver(sf);
        this.addKeyListener(new KeyAction(g));
    }

    @Override
    public Dimension getPreferredSize() {
        return model.map.view.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        model.map.view.setLocation(0, 0);
        super.paintComponent(g);
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
}

public class Game {
    public GameModel model;
    public GameSimpleView view;
    public GameController controller;

    public Game() {
        model = new GameModel();
        controller = new GameController(model);
        view = new GameSimpleView(this);
    }
}

