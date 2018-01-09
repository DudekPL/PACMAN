package com.company.model;


import com.company.util.enums.Direction;

import java.awt.*;

public class PacmanModel extends GhostModel {
    private Direction nextmove;

    public PacmanModel(int x, int y) {
        super(x, y, 0, 0, Color.RED);
        nextmove = Direction.NONE;
    }

    public Direction getNextmove() {
        return nextmove;
    }

    public synchronized void setNextmove(Direction nextmove) {
        this.nextmove = nextmove;
    }
}