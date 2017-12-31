package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

enum Inside {
    EMPTY, DOT, BIGDOT, FRUIT
}

class FieldModel {
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

    public Inside getInside() {
        return inside;
    }

    public void setInside(Inside i) {
        inside = i;
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

class FieldSimpleView extends JComponent {
    private FieldModel model;
    public static final int FIELD_SIZE = 40;
    private static final int WALL_WIDTH = 5;

    public FieldSimpleView(FieldModel m) {
        model = m;
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
        if (model.getInside() == Inside.DOT)
            g2.fill(new Ellipse2D.Float((float)0.5*FIELD_SIZE - r, (float)0.5 * FIELD_SIZE - r, 2 * r, 2 * r));
        if (model.getInside() == Inside.BIGDOT)
            g2.fill(new Ellipse2D.Float((float) 0.5 * FIELD_SIZE - 2 * r, (float) 0.5 * FIELD_SIZE - 2 * r, 4 * r, 4 * r));
        if (model.getInside() == Inside.FRUIT) {
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

    public Field() {
        model = new FieldModel();
        view = new FieldSimpleView(model);
    }
}