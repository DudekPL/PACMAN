package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

class PacmanModel extends GhostModel {
    private Direction nextmove;

    public PacmanModel(int x, int y) {
        super(x, y, 0, 0);
        nextmove = Direction.NONE;
    }

    public Direction getNextmove() {
        return nextmove;
    }

    public void setNextmove(Direction nextmove) {
        this.nextmove = nextmove;
    }
}


class PacmanController extends GhostController {
    public PacmanController(PacmanModel pm, Map m, int rx, int ry) {
        super(pm, m, rx, ry);
    }

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

    public void setnextMove(Direction d){
        int x = model.getPosx();
        int y = model.getPosy();
        if (d == Direction.RIGHT && !map.model.field(x,y).model.canRight()) return;
        if (d == Direction.LEFT && !map.model.field(x,y).model.canLeft()) return;
        if (d == Direction.DOWN && !map.model.field(x,y).model.canDown()) return;
        if (d == Direction.UP && !map.model.field(x,y).model.canUp()) return;
        ((PacmanModel)model).setNextmove(d);}
    }


public class Pacman{
    public PacmanModel model;
    public PacmanSimpleView view;
    public PacmanController controller;



    public Pacman(Map m, int x, int y, int size, long tfm) {
        model = new PacmanModel(x, y);
        view = new PacmanSimpleView(model, size, tfm);
        controller = new PacmanController(model, m, x, y);
        model.addObserver(view);
    }
}