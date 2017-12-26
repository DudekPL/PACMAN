package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends JComponent{
    int score;
    boolean paused;
    Map map;
   // private Pacman player;
    List<Ghost> ghosts;

    public Game(){
        score = 0;
        paused = false;
        map = new Map();
        ghosts = new ArrayList<>();
        ghosts.add(new Ghost(7, 4, Color.RED, map.view.FIELD_SIZE, 1000));
    }
    public void paintComponent(Graphics g){
        map.view.paintComponent(g);
        for (Ghost gh: ghosts) {
            gh.view.paintComponent(g);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return map.view.getPreferredSize();
    }
}
