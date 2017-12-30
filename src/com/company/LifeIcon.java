package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Observable;
import java.util.Observer;

public class LifeIcon extends JComponent implements Observer {
    private int fieldsize;
    private int icosize;
    private int lives;
    private int posx;
    private int posy;

    public LifeIcon(int x, int y, int l, int fs, int is){
        super();
        fieldsize = fs;
        icosize = is;
        posx = x;
        posy = y;
        lives = l;
    }

    @Override
    protected void paintComponent(Graphics g) {
        setLocation(posx, posy);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.RED);
        int r = icosize/2;
        for (int i = 0; i < lives; i++) {
            g2.fill(new Ellipse2D.Float((float) ((float)i + 0.5) * fieldsize - 2 * r, (float) 0.5 * fieldsize - 2 * r, 4 * r, 4 * r));
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(lives*fieldsize, fieldsize);
    }

    @Override
    public void update(Observable o, Object arg) {
        lives = ((GameModel)o).getLives();
    }
}