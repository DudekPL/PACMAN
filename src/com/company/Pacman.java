package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
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


    public PacmanSimpleView(PacmanModel m, int sz, int tfm) {
        super(m, Color.YELLOW, sz, tfm);
        direction = Direction.NONE;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        double theta = 0.0;
        if (direction == Direction.LEFT) theta = Math.PI;
        if (direction == Direction.UP) theta = Math.PI * 1.5;
        if (direction == Direction.DOWN) theta = Math.PI / 2;
        g2.rotate(theta, fieldsize/2, fieldsize/2);
        display();
        int toa = 800;
        String path = "img/Pacman/pacman1.png";
        long t = System.currentTimeMillis() % toa;
        if (t>toa/4) path = "img/Pacman/pacman2.png";
        if (t>2*toa/4) path = "img/Pacman/pacman3.png";
        if (t>3*toa/4) path = "img/Pacman/pacman4.png";
        if (direction == Direction.NONE) path = "img/Pacman/pacman1.png";
        ImageIcon img = new ImageIcon(path);
        g2.scale(fieldsize/((float)img.getIconWidth()), fieldsize/((float)img.getIconHeight()));
        g2.drawImage(img.getImage(), 0,0 ,null);
    }

    @Override
    public void update(Observable obs, Object obj) {
        direction = ((PacmanModel)model).getNextmove();
        super.update(obs, obj);
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



    public Pacman(Map m, int x, int y, int size, int tfc) {
        model = new PacmanModel(x, y);
        view = new PacmanSimpleView(model, size, tfc);
        controller = new PacmanController(model, m, x, y);
        model.addObserver(view);
    }
}