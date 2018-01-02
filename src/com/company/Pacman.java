package com.company;

import java.awt.*;

class PacmanModel extends GhostModel {
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


class PacmanController extends GhostController {
    public PacmanController(PacmanModel pm, PacmanSimpleView v, Map m, int rx, int ry) {
        super(pm, v, m, rx, ry);
    }

    @Override
    public void respawn() {
        synchronized (model) {
            ((PacmanModel)model).setNextmove(Direction.NONE);
            super.respawn();
        }
    }

    public void move() {
        synchronized (model) {
            Direction d = ((PacmanModel)model).getNextmove();
            int x = model.getPosx();
            int y = model.getPosy();
            if (d == Direction.RIGHT && map.model.field(x,y).model.canRight()) {model.changePos(d); return;}
            if (d == Direction.LEFT && map.model.field(x,y).model.canLeft()) {model.changePos(d); return;}
            if (d == Direction.DOWN && map.model.field(x,y).model.canDown()) {model.changePos(d); return;}
            if (d == Direction.UP && map.model.field(x,y).model.canUp()) {model.changePos(d); return;}
        }
    }

    public void setnextMove(Direction d){
        int x = model.getPosx();
        int y = model.getPosy();
        if (d == Direction.RIGHT && !map.model.field(x,y).model.canRight()) return;
        if (d == Direction.LEFT && !map.model.field(x,y).model.canLeft()) return;
        if (d == Direction.DOWN && !map.model.field(x,y).model.canDown()) return;
        if (d == Direction.UP && !map.model.field(x,y).model.canUp()) return;
        ((PacmanModel)model).setNextmove(d);
    }
}


public class Pacman{
    public PacmanModel model;
    public PacmanSimpleView view;
    public PacmanController controller;



    public Pacman(Map m, int x, int y, int size, long tfm) {
        model = new PacmanModel(x, y);
        view = new PacmanSimpleView(model, size, tfm);
        controller = new PacmanController(model, view, m, x, y);
        model.addObserver(view);
    }
}