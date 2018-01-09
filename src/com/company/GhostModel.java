package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class GhostModel extends Observable {
    protected volatile int posx;
    protected volatile int posy;
    long timeforeating, timeforrespawning;
    private volatile State status;
    protected volatile  int actposx;
    protected volatile int actposy;
    private final Color color;

    public GhostModel(int x, int y, long tfe, long tfr, Color c) {
        posx = x;
        posy = y;
        status = State.RESPAWNING;
        timeforeating = tfe;
        timeforrespawning = tfr;
        Timer t = new Timer(0, event->status=State.CHASING);
        t.setInitialDelay((int)timeforrespawning);
        t.setRepeats(false);
        t.start();
        color = c;
    }

    public int getActposx() {
        return actposx;
    }

    public int getActposy() {
        return actposy;
    }

    public Color getColor() {
        return color;
    }

    public synchronized void setActPos(int x, int y) {actposx = x; actposy = y;}

    public synchronized int getPosx() {
        return posx;
    }

    public synchronized int getPosy() {
        return posy;
    }

    public synchronized void changePos(Direction d) {
        switch (d) {
            case UP:
                posy = posy - 1;
                break;
            case DOWN:
                posy = posy + 1;
                break;
            case LEFT:
                posx = posx - 1;
                break;
            case RIGHT:
                posx = posx + 1;
                break;
        }
        setChanged();
        notifyObservers(d);
    }

    public synchronized void respawn(int x, int y) {
        posx = x;
        posy = y;
        status = State.RESPAWNING;
        Timer t = new Timer(0, event->status=State.CHASING);
        t.setInitialDelay((int)timeforrespawning);
        t.setRepeats(false);
        t.start();
        setChanged();
        notifyObservers(Direction.NONE);
    }

    public synchronized State getStatus() {
        return status;
    }

    public void makeEatable() {
        status = State.EATABLE;
        Timer t = new Timer(0, event-> {if (status == State.EATABLE) status=State.CHASING;});
        t.setInitialDelay((int)timeforrespawning);
        t.setRepeats(false);
        t.start();
        setChanged();
        notifyObservers(Direction.NONE);
    }
}