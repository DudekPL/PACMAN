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
        ghosts.add(new Ghost(map,7, 4, Color.RED, map.view.FIELD_SIZE, 1000));
        player = new Pacman(map, 8, 6, map.view.FIELD_SIZE, 1000);
        map.model.field(8, 6).setInside(Inside.EMPTY);
    }
}

class GameSimpleView extends JComponent{
    private GameModel model;

    public GameSimpleView(GameModel m) {model = m;}
    public void paintComponent(Graphics g) {
        model.map.view.paintComponent(g);
        for (Ghost gh : model.ghosts) {
            gh.view.paintComponent(g);
        }
        model.player.view.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return model.map.view.getPreferredSize();
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