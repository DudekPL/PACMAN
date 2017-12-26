package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import static com.company.Direction.*;

enum State{
    CHASING, EATABLE, RESPAWNING
}

enum Direction{
    UP, DOWN, RIGHT, LEFT, NONE
}


class GhostModel extends Observable{
    private State status;
    private int posx;
    private int posy;

    public GhostModel(int x, int y) {
        posx=x;
        posy=y;
        status = State.RESPAWNING;
    }
    public int getPosx() {return posx;}
    public int getPosy() {return posy;}
    public void changePos (Direction d){
        switch (d) {
            case UP:    posy=posy-1;
                        break;
            case DOWN:  posy=posy+1;
                        break;
            case LEFT:  posx=posx-1;
                        break;
            case RIGHT: posx=posx+1;
                        break;
        }
        setChanged();
        notifyObservers(d);
    }
    public State getStatus() {return status;}
    public void setStatus(State s) {status = s;}
}

class GhostSimpleView extends JComponent implements Observer{
    private GhostModel model;
    private Color color;
    private long timeofchange;
    private float timeforchange;
    private int ghostsize = 40;
    private int prevposx;
    private int prevposy;

    public GhostSimpleView (GhostModel m, Color c, int sz, int tfc) {
        model = m;
        color = c;
        ghostsize = sz;
        prevposx = m.getPosx();
        prevposy = m.getPosy();
        timeforchange = tfc;
        timeofchange = System.currentTimeMillis();
    }
    public void paintComponent (Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        float time = System.currentTimeMillis()-timeofchange;
        if (time > timeforchange) time = timeforchange;
        float posx = (float)ghostsize*(float)(model.getPosx()-prevposx)*(time/timeforchange);
        posx = posx + prevposx*ghostsize;
        float posy = (float)ghostsize*(float)(model.getPosy()-prevposy)*(time/timeforchange);
        posy = posy + prevposy*ghostsize;
        Rectangle2D ghost = new Rectangle2D.Float(posx, posy, ghostsize, ghostsize);
        g2.setPaint(color);
        g2.fill(ghost);
    }
    public void update(Observable obs, Object obj){
        if (obs == model) {
            timeofchange = System.currentTimeMillis();
            switch ((Direction) obj) {
                case UP:    prevposy=model.getPosy()+1;
                    prevposx=model.getPosx();
                    break;
                case DOWN:  prevposy=model.getPosy()-1;
                    prevposx=model.getPosx();
                    break;
                case LEFT:  prevposx=model.getPosx()+1;
                    prevposy=model.getPosy();
                    break;
                case RIGHT: prevposx=model.getPosx()-1;
                    prevposy=model.getPosy();
                    break;
                case NONE: prevposx=model.getPosx();
                    prevposy=model.getPosy();
                    break;
            }
        }
    }
}

public class Ghost {
    public GhostModel model;
    public GhostSimpleView view;

    public Ghost(int x, int y, Color c, int size, int tfc){
        model = new GhostModel(x, y);
        view = new GhostSimpleView(model, c, size, tfc);
        model.addObserver(view);
    }
}
