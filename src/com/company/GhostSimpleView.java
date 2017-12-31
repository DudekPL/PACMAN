package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class GhostSimpleView extends JComponent implements Observer {
    protected GhostModel model;
    protected int fieldsize = 40;
    protected Direction direction;
    private long timeofchange;
    private float timeformove;
    private int prevposx;
    private int prevposy;
    private int actposy;
    private int actposx;
    private String path;
    private long timeofeating;

    public GhostSimpleView(GhostModel m, Color c, int sz, long tfm) {
        model = m;
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

    static private String getPath(Color c) {
        String p = "img/Ghosts/Pinky/";
        if (c == Color.RED) p = "img/Ghosts/Blinky/";
        if (c == Color.BLUE) p = "img/Ghosts/Inky/";
        if (c == Color.YELLOW || c == Color.ORANGE) p = "img/Ghosts/Clyde/";
        return p;
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
        if (status == State.EATABLE) {
            if(System.currentTimeMillis() - timeofeating > 0.8*model.timeforeating) {
                long t = System.currentTimeMillis() % 200;
                if (t>100) img = new ImageIcon("img/Ghosts/Eatable/2.png");
            }
        }
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
            if(((GhostModel)obs).getStatus() != State.EATABLE)  timeofeating = 0;
            else timeofeating = System.currentTimeMillis();
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