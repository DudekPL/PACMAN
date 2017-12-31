package com.company;

import java.awt.*;
import java.util.Observable;
import javax.swing.*;

enum State {
    CHASING, EATABLE, RESPAWNING
}

enum Direction {
    UP, DOWN, RIGHT, LEFT, NONE
}


class GhostModel extends Observable {
    protected int posx;
    protected int posy;
    long timeforeating, timeforrespawning;
    private State status;

    public GhostModel(int x, int y, long tfe, long tfr) {
        posx = x;
        posy = y;
        status = State.RESPAWNING;
        timeforeating = tfe;
        timeforrespawning = tfr;
        Timer t = new Timer(0, event->status=State.CHASING);
        t.setInitialDelay((int)timeforrespawning);
        t.setRepeats(false);
        t.start();
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public void changePos(Direction d) {
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

    public void respawn(int x, int y) {
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

    public State getStatus() {
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


class GhostController {
    protected GhostModel model;
    protected Map map;
    protected int respx;
    protected int respy;

    public GhostController(GhostModel gm, Map m, int rx, int ry) {
        model = gm;
        map = m;
        respx = rx;
        respy = ry;
    } //TODO nextMoves

    public Direction nextMoveChasing() {
        return Direction.NONE;
    }

    public Direction nextMoveEatable() {
        return Direction.NONE;
    }

    public Direction nextMoveRespawning() {
        return Direction.NONE;
    }

    public void move() {
        Direction d = Direction.NONE;
        if (model.getStatus() == State.CHASING) d = nextMoveChasing();
        if (model.getStatus() == State.EATABLE) d = nextMoveEatable();
        if (model.getStatus() == State.RESPAWNING) d = nextMoveRespawning();
        model.changePos(d);
    }

    public void makeEatable() {
        if (model.getStatus() != State.RESPAWNING) model.makeEatable();
    }

    public boolean collided() {
        boolean point = false;
        if (model.getStatus() == State.EATABLE) point = true;
        model.respawn(respx, respy);
        return point;
    }

    public void respawn() {model.respawn(respx, respy);}
}

public class Ghost {
    public GhostModel model;
    public GhostSimpleView view;
    public GhostController controller;

    public Ghost(Map m, int x, int y, Color c, int size, long tfm, long tfe, long tfr) {
        model = new GhostModel(x, y, tfe, tfr);
        view = new GhostSimpleView(model, c, size, tfm);
        controller = new GhostController(model, m, x, y);
        model.addObserver(view);
    }
}