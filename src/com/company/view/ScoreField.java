package com.company.view;

import com.company.model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ScoreField extends JComponent implements Observer {
    private int score;
    private int fieldsize;
    private int posx;
    private int posy;

    public ScoreField(GameModel gm, int x, int y) {
        super();
        posx = x;
        posy = y;
        fieldsize = gm.map.view.FIELD_SIZE;
        score = gm.getScore();
        gm.addObserver(this);
        setLocation(posx, posy);
    }

    @Override
    public void update(Observable o, Object arg) {
        int s = ((GameModel) o).getScore();
        Timer t = new Timer(0, event->score = s);
        t.setInitialDelay((int)((GameModel) o).timeformove);
        t.setRepeats(false);
        t.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(6*fieldsize, fieldsize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setLocation(posx*fieldsize,posy*fieldsize);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        StringBuilder sb = new StringBuilder();
        sb.append(score);
        while(sb.length()<6) sb.insert(0,0);
        String s = sb.toString();
        ((Graphics2D) g).setPaint(Color.WHITE);
        g.drawString(s,10,30);
    }
}
