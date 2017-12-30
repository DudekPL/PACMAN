package com.company;

import javax.swing.*;
import java.awt.*;
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
    private long timeforeating;
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
    protected GhostModel model;
    private Color color;
    private long timeofchange;
    private float timeformove;
    protected int fieldsize = 40;
    protected Direction direction;
    private int prevposx;
    private int prevposy;
    private int actposy;
    private int actposx;
    private String path;
    private long timeofeating;

    public GhostSimpleView(GhostModel m, Color c, int sz, int tfm) {
        model = m;
        color = c;
        fieldsize = sz;
        prevposx = m.getPosx();
        prevposy = m.getPosy();
        timeformove = tfm;
        timeofchange = System.currentTimeMillis();
        actposx = prevposx;
        actposy = prevposy;
        path = getPath(c);
        direction = Direction.UP;
    }

    protected void display() {
        setLocation(actposx, actposy);
        float time = System.currentTimeMillis() - timeofchange;
        if (time > timeformove) time = timeformove;
        float posx = (float) fieldsize * (float) (model.getPosx() - prevposx) * (time / timeformove);
        actposx = (int)posx + prevposx * fieldsize;
        float posy = (float) fieldsize * (float) (model.getPosy() - prevposy) * (time / timeformove);
        actposy = (int)posy + prevposy * fieldsize;
    }

    static private String getPath(Color c) {
        String p = "img/Ghosts/Pinky/";
        if (c == Color.RED) p = "img/Ghosts/Blinky/";
        if (c == Color.BLUE) p = "img/Ghosts/Inky/";
        if (c == Color.YELLOW || c == Color.ORANGE) p = "img/Ghosts/Clyde/";
        return p;
    }

    @Override
    public void paintComponent(Graphics g) {
        display();
        Graphics2D g2 = (Graphics2D) g;
        ImageIcon img = new ImageIcon("img/Ghosts/Eatable/1.png");
        StringBuilder pathbuild = new StringBuilder();
        State status = model.getStatus();
        if (status == State.CHASING) {
            pathbuild.append(path);
            if (direction == Direction.RIGHT) pathbuild.append("R.png");
            if (direction == Direction.LEFT) pathbuild.append("L.png");
            if (direction == Direction.DOWN) pathbuild.append("D.png");
            if (direction == Direction.UP || direction == Direction.NONE) pathbuild.append("U.png");
            img = new ImageIcon(pathbuild.toString());
        }
        if (status == State.RESPAWNING) {
            pathbuild.append("img/Ghosts/Resp/");
            if (direction == Direction.RIGHT) pathbuild.append("R.png");
            if (direction == Direction.LEFT) pathbuild.append("L.png");
            if (direction == Direction.DOWN) pathbuild.append("D.png");
            if (direction == Direction.UP || direction == Direction.NONE) pathbuild.append("U.png");
            img = new ImageIcon(pathbuild.toString());
        }
        if (status == State.EATABLE)
        g2.scale(fieldsize/((float)img.getIconWidth()), fieldsize/((float)img.getIconHeight()));
        g2.drawImage(img.getImage(), 0,0 ,null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(fieldsize, fieldsize);
    }

    @Override
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
            direction = (Direction) obj;
        }
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
    public GhostController controller;

    public Ghost(Map m, int x, int y, Color c, int size, int tfc) {
        model = new GhostModel(x, y);
        view = new GhostSimpleView(model, c, size, tfc);
        controller = new GhostController(model, m, x, y);
        model.addObserver(view);
    }
}