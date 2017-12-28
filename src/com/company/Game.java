package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class GameModel{
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
        ghosts.add(new Ghost(map, 7, 4, Color.RED, map.view.FIELD_SIZE, 1000));
        player = new Pacman(map, 8, 6, map.view.FIELD_SIZE, 1000);
        map.model.field(8, 6).model.setInside(Inside.EMPTY);
    }
}

class GameSimpleView extends JDesktopPane{
    private GameModel model;

    public GameSimpleView(GameModel m) {
        setLayout(new FlowLayout());
        model = m;
        setDesktopManager(new DefaultDesktopManager());
        add(model.map.view);
        moveToBack(model.map.view);
        for (Ghost gh: model.ghosts) {
            add(gh.view);
            gh.view.display();
            moveToFront(gh.view);
            gh.view.setGameview(this);
            System.out.println(getLayer(model.map.view)-getLayer(gh.view));
        }
        model.map.view.setLocation(0, 0);
        setOpaque(false);
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

public class Game {
    public GameModel model;
    public GameSimpleView view;

    public Game() {
        model = new GameModel();
        view = new GameSimpleView(model);
    }
}