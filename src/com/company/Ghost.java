package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;


enum State {
    CHASING, EATABLE, RESPAWNING
}

enum Direction {
    UP, DOWN, RIGHT, LEFT, NONE
}


class GhostModel extends Observable {
    protected int posx;
    protected int posy;
    private State status;

    public GhostModel(int x, int y) {
        posx = x;
        posy = y;
        status = State.RESPAWNING;
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
        setChanged();
        notifyObservers(Direction.NONE);
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State s) {
        status = s;
    }
}

class GhostSimpleView extends JComponent implements Observer {
    private GhostModel model;
    private Color color;
    private long timeofchange;
    private float timeforchange;
    private int ghostsize = 40;
    private int prevposx;
    private int prevposy;

    public GhostSimpleView(GhostModel m, Color c, int sz, int tfc) {
        model = m;
        color = c;
        ghostsize = sz;
        prevposx = m.getPosx();
        prevposy = m.getPosy();
        timeforchange = tfc;
        timeofchange = System.currentTimeMillis();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        float time = System.currentTimeMillis() - timeofchange;
        if (time > timeforchange) time = timeforchange;
        float posx = (float) ghostsize * (float) (model.getPosx() - prevposx) * (time / timeforchange);
        posx = posx + prevposx * ghostsize;
        float posy = (float) ghostsize * (float) (model.getPosy() - prevposy) * (time / timeforchange);
        posy = posy + prevposy * ghostsize;
        Rectangle2D ghost = new Rectangle2D.Float(posx, posy, ghostsize, ghostsize);
        g2.setPaint(color);
        g2.fill(ghost);
    }

    public void update(Observable obs, Object obj) {
        if (obs == model) {
            timeofchange = System.currentTimeMillis();
            switch ((Direction) obj) {
                case UP:
                    prevposy = model.getPosy() + 1;
                    prevposx = model.getPosx();
                    break;
                case DOWN:
                    prevposy = model.getPosy() - 1;
                    prevposx = model.getPosx();
                    break;
                case LEFT:
                    prevposx = model.getPosx() + 1;
                    prevposy = model.getPosy();
                    break;
                case RIGHT:
                    prevposx = model.getPosx() - 1;
                    prevposy = model.getPosy();
                    break;
                case NONE:
                    prevposx = model.getPosx();
                    prevposy = model.getPosy();
                    break;
            }
        }
    }
}

class GhostControler {
    protected GhostModel model;
    protected Map map;
    protected int respx;
    protected int respy;

    public GhostControler(GhostModel gm, Map m, int rx, int ry) {
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

    public boolean collided() {
        boolean point = false;
        if (model.getStatus() == State.EATABLE) point = true;
        model.respawn(respx, respy);
        return point;
    }
}

public class Ghost {
    public GhostModel model;
    public GhostSimpleView view;
    public GhostControler controler;

    public Ghost(Map m, int x, int y, Color c, int size, int tfc) {
        model = new GhostModel(x, y);
        view = new GhostSimpleView(model, c, size, tfc);
        controler = new GhostControler(model, m, x, y);
        model.addObserver(view);
    }
}