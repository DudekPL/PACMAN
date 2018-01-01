package com.company;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Observable;
import javax.swing.*;

enum State {
    CHASING, EATABLE, RESPAWNING
}

enum Direction {
    UP, DOWN, RIGHT, LEFT, NONE
}


class GhostModel extends Observable {
    protected volatile int posx;
    protected volatile int posy;
    long timeforeating, timeforrespawning;
    private volatile State status;

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

    public Direction nextMoveChasing(int x, int y) {
        int posx,posy;
        posx = model.posx;
        posy = model.posy;
        return BFS(posx, posy, 12, x, y);
    }
    class Vertex{
        Dimension coords;
        int depth;
        Vertex parent;

        Vertex(int x, int y, int d, Vertex p) {
            coords = new Dimension(x, y);
            depth = d;
            parent = p;
        }

        Vertex(int x, int y) {
            depth = 0;
            coords = new Dimension(x,y);
            parent = null;
        }
    }


    private Direction BFS(int firstx, int firsty, int maxdepth, int pacx, int pacy) {
        Queue<Vertex> q = new LinkedList<>();
        Vertex v = new Vertex(firstx, firsty);
        q.offer(v);
        while(!q.isEmpty()) {
            v = q.poll();
            if (v.coords.width == pacx && v.coords.height == pacy) break;
            if (v.depth<maxdepth) {
                if (map.model.field(v.coords.width, v.coords.height).model.canDown()) q.offer(new Vertex(v.coords.width, v.coords.height+1,v.depth+1, v));
                if (map.model.field(v.coords.width, v.coords.height).model.canUp()) q.offer(new Vertex(v.coords.width, v.coords.height-1,v.depth+1, v));
                if (map.model.field(v.coords.width, v.coords.height).model.canLeft()) q.offer(new Vertex(v.coords.width-1, v.coords.height,v.depth+1, v));
                if (map.model.field(v.coords.width, v.coords.height).model.canRight()) q.offer(new Vertex(v.coords.width+1, v.coords.height,v.depth+1, v));
            }
        }
        while(v != null && v.parent !=null && (v.parent.coords.width != firstx || v.parent.coords.height != firsty)) {
            if (v == null) break;
            v = v.parent;
        }
        if (v == null) return Direction.NONE;
        if (v.coords.height - firsty == 1) return Direction.DOWN;
        if (v.coords.height - firsty == -1) return Direction.UP;
        if (v.coords.width - firstx == 1) return Direction.RIGHT;
        if (v.coords.width - firstx == -1) return Direction.LEFT;
        return Direction.NONE;
    }

    public Direction nextMoveEatable(int x, int y) {
        synchronized (model) {
            return Direction.NONE;
        }
    }

    public Direction nextMoveRespawning() {
        synchronized (model) {
            return Direction.NONE;
        }
    }

    public void move(int x, int y) {
        Direction d = Direction.NONE;
        synchronized (model) {
            if (model.getStatus() == State.CHASING) d = nextMoveChasing(x, y);
            if (model.getStatus() == State.EATABLE) d = nextMoveEatable(x, y);
            if (model.getStatus() == State.RESPAWNING) d = nextMoveRespawning();
            model.changePos(d);
        }
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