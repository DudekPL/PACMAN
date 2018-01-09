package com.company;

import java.util.Observable;

public class FieldModel extends Observable {
    private Inside inside;
    private boolean up;
    private boolean down;
    private boolean right;
    private boolean left;

    public FieldModel(Inside i, boolean u, boolean d, boolean r, boolean l) {
        inside = i;
        up = u;
        down = d;
        right = r;
        left = l;
    }

    public FieldModel() {
        inside = Inside.DOT;
        up = true;
        down = true;
        right = true;
        left = true;
    }

    public synchronized Inside getInside() {
        return inside;
    }

    public synchronized void setInside(Inside i) {
        inside = i;
        setChanged();
        notifyObservers();
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
        return (inside == Inside.EMPTY);
    }

}