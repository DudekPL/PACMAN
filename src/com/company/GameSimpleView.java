package com.company;

import javax.swing.*;
import java.awt.*;

public class GameSimpleView extends JDesktopPane {
    private GameModel model;

    public GameSimpleView(Game g) {
        GameModel m=g.model;
        setLayout(new FlowLayout());
        model = m;
        setDesktopManager(new DefaultDesktopManager());
        add(model.map.view);
        moveToBack(model.map.view);
        for (Ghost gh: model.ghosts) {
            add(gh.view);
            gh.view.display();
            moveToFront(gh.view);
        }
        add(model.player.view);
        model.player.view.display();
        moveToFront(model.player.view);
        model.map.view.setLocation(0, 0);
        setOpaque(false);
        LifeIcon lico = new LifeIcon(6*model.map.view.FIELD_SIZE, 7*model.map.view.FIELD_SIZE, model.lives, model.map.view.FIELD_SIZE, 8);
        add(lico);
        lico.setLocation(6*model.map.view.FIELD_SIZE, 7*model.map.view.FIELD_SIZE);
        moveToFront(lico);
        m.addObserver(lico);
        ScoreField sf = new ScoreField(model, 6, 1);
        add(sf);
        moveToFront(sf);
        m.addObserver(sf);
        this.addKeyListener(new KeyAction(g));
    }

    @Override
    public Dimension getPreferredSize() {
        return model.map.view.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        model.map.view.setLocation(0, 0);
        super.paintComponent(g);
    }
}
