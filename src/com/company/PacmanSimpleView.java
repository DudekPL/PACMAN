package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PacmanSimpleView extends GhostSimpleView implements Observer {


    public PacmanSimpleView(PacmanModel m, int sz, long tfm) {
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