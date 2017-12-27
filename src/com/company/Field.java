package com.company;

enum Inside {
    EMPTY, DOT, BIGDOT, FRUIT;
}

public class Field {
    private Inside inside;
    private boolean up;
    private boolean down;
    private boolean right;
    private boolean left;

    public Field(Inside i, boolean u, boolean d, boolean r, boolean l) {
        inside = i;
        up = u;
        down = d;
        right = r;
        left = l;
    }

    public Field() {
        inside = Inside.DOT;
        up = true;
        down = true;
        right = true;
        left = true;
    }

    public Inside getInside() {
        return inside;
    }

    public void setInside(Inside i) {
        inside = i;
    }

    public boolean canUp() {
        return up;
    }

    public boolean canDown() {
        return down;
    }

    public boolean canRight() {
        return right;
    }

    public boolean canLeft() {
        return left;
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void setDown(boolean b) {
        down = b;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public boolean isEmpty() {
        if(inside == Inside.EMPTY) return true;
        else return false;
    }

}
