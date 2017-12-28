package com.company;

import java.awt.*;
import java.util.Observer;

class PacmanModel extends GhostModel {
    private Direction nextmove;

    public PacmanModel(int x, int y) {
        super(x, y);
        nextmove = Direction.NONE;
    }

    public void setNextmove(Direction nextmove) {
        this.nextmove = nextmove;
    }

    public Direction getNextmove() {
        return nextmove;
    }
}


class PacmanSimpleView extends GhostSimpleView implements Observer {
    public PacmanSimpleView(PacmanModel m, int sz, int tfc) {
        super(m, Color.YELLOW, sz, tfc);
    }
}


class PacmanController extends GhostController {
    @Override
    public void move() {
        Direction d = ((PacmanModel)model).getNextmove();
        int x = model.getPosx();
        int y = model.getPosy();
        if (d == Direction.RIGHT && map.model.field(x,y).model.canRight()) {model.changePos(d); return;}
        if (d == Direction.LEFT && map.model.field(x,y).model.canLeft()) {model.changePos(d); return;}
        if (d == Direction.DOWN && map.model.field(x,y).model.canDown()) {model.changePos(d); return;}
        if (d == Direction.UP && map.model.field(x,y).model.canUp()) {model.changePos(d); return;}
    }
    public PacmanController(PacmanModel pm, Map m, int rx, int ry) {
        super(pm, m, rx, ry);
    }
}


public class Pacman{
    public PacmanModel model;
    public PacmanSimpleView view;
    public PacmanController controller;

    public Pacman(Map m, int x, int y, int size, int tfc) {
        model = new PacmanModel(x, y);
        view = new PacmanSimpleView(model, size, tfc);
        controller = new PacmanController(model, m, x, y);
        model.addObserver(view);
    }
}