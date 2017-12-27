package com.company;

class PacmanModel extends GhostModel {
    private Direction nextmove;

    public PacmanModel(int x, int y) {
        super(x, y);
        nextmove = Direction.NONE;
    }
}

public class Pacman {
    private PacmanModel model;
}