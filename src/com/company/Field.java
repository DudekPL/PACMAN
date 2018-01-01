package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

enum Inside {
    EMPTY, DOT, BIGDOT, FRUIT
}

class FieldModel extends Observable{
    private Inside inside;
    private boolean up;
    private boolean down;
    private boolean right;
    private boolean left;

    public FieldModel(Inside i, boolean u, boolean d, boolean r, boolean l) {
        inside = i;
        up = u;
        down = d;
        right = r;
        left = l;
    }

    public FieldModel() {
        inside = Inside.DOT;
        up = true;
        down = true;
        right = true;
        left = true;
    }

    public synchronized Inside getInside() {
        return inside;
    }

    public synchronized void setInside(Inside i) {
        inside = i;
        setChanged();
        notifyObservers();
    }

    public boolean canUp() {
        return up;
    }

    public boolean canDown() {
        return down;
    }

    public boolean canRight() {
        return right;
    }

    public boolean canLeft() {
        return left;
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void setDown(boolean b) {
        down = b;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public boolean isEmpty() {
        return (inside == Inside.EMPTY);
    }

}

class FieldController {
    private FieldModel model;

    public FieldController(FieldModel m) {model = m;}
    public Inside eat() {
        Inside pom = model.getInside();
        model.setInside(Inside.EMPTY);
        return pom;
    }
}

class FieldSimpleView extends JComponent implements Observer {
    public static final int FIELD_SIZE = 40;
    private static final int WALL_WIDTH = 5;
    private FieldModel model;
    private Inside inside;
    private int timeformove = 800;

    public void Timeformove(int tom) {timeformove = tom;}

    public FieldSimpleView(FieldModel m) {
        model = m;
    }

    @Override
    public void update(Observable o, Object arg) {
        Inside in = ((FieldModel)o).getInside();
        if (in == Inside.DOT || in == Inside.BIGDOT)
        inside = in;
        else{
            Timer t = new Timer(0, event->inside = in);
            t.setInitialDelay(timeformove);
            t.setRepeats(false);
            t.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        int r = WALL_WIDTH/2;
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.BLACK);
        g2.fill(new Rectangle2D.Float(0, 0, FIELD_SIZE, FIELD_SIZE));
        g2.setPaint(Color.BLUE);
        if (!model.canUp())
            g2.fill(new Rectangle2D.Float(0, 0, FIELD_SIZE, WALL_WIDTH));
        if (!model.canDown())
            g2.fill(new Rectangle2D.Float(0, FIELD_SIZE - WALL_WIDTH, FIELD_SIZE, WALL_WIDTH));
        if (!model.canLeft())
            g2.fill(new Rectangle2D.Float(0, 0, WALL_WIDTH, FIELD_SIZE));
        if (!model.canRight())
            g2.fill(new Rectangle2D.Float(FIELD_SIZE - WALL_WIDTH, 0, WALL_WIDTH, FIELD_SIZE));
        g2.setPaint(Color.WHITE);
        if (inside == Inside.DOT || (inside == Inside.EMPTY && model.getInside() == Inside.DOT))
            g2.fill(new Ellipse2D.Float((float)0.5*FIELD_SIZE - r, (float)0.5 * FIELD_SIZE - r, 2 * r, 2 * r));
        if (inside == Inside.BIGDOT || (inside == Inside.EMPTY && model.getInside() == Inside.BIGDOT))
            g2.fill(new Ellipse2D.Float((float) 0.5 * FIELD_SIZE - 2 * r, (float) 0.5 * FIELD_SIZE - 2 * r, 4 * r, 4 * r));
        if (inside == Inside.FRUIT || (inside == Inside.EMPTY && model.getInside() == Inside.FRUIT)) {
            ImageIcon img = new ImageIcon("img/Icons/Cherry.png");
            g2.scale(FIELD_SIZE/((float)img.getIconWidth()), FIELD_SIZE/((float)img.getIconHeight()));
            g2.drawImage(img.getImage(), 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(FIELD_SIZE, FIELD_SIZE);
    }
}

public class Field {
    public FieldModel model;
    public FieldSimpleView view;
    public FieldController controller;

    public Field() {
        model = new FieldModel();
        view = new FieldSimpleView(model);
        controller = new FieldController(model);
        model.addObserver(view);
    }
}