package com.company.view;

import com.company.model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class LifeIcon extends JComponent implements Observer {
    private int fieldsize;
    private int lives;
    private int posx;
    private int posy;

    public LifeIcon(int x, int y, int l, int fs){
        super();
        fieldsize = fs;
        posx = x;
        posy = y;
        lives = l;
    }

    @Override
    protected void paintComponent(Graphics g) {
        setLocation(posx, posy);
        Graphics2D g2 = (Graphics2D) g;
        ImageIcon img = new ImageIcon("img/Icons/LifeIcon.png");
        g2.scale(fieldsize/((float)img.getIconWidth()), fieldsize/((float)img.getIconHeight()));
        for (int i = 0; i < lives; i++) {
            g2.drawImage(img.getImage(), i*img.getIconHeight()/2,0 ,null);
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