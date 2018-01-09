package com.company.view;

import com.company.model.GameModel;
import com.company.util.keys.KeyActionGame;
import com.company.wrapper.Game;
import com.company.wrapper.Ghost;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class GameSimpleView extends JDesktopPane implements Observer{
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
        LifeIcon lico = new LifeIcon(6*model.map.view.FIELD_SIZE, 7*model.map.view.FIELD_SIZE, model.getLives(), model.map.view.FIELD_SIZE, 8);
        add(lico);
        lico.setLocation(6*model.map.view.FIELD_SIZE, 7*model.map.view.FIELD_SIZE);
        moveToFront(lico);
        m.addObserver(lico);
        ScoreField sf = new ScoreField(model, 6, 1);
        add(sf);
        moveToFront(sf);
        m.addObserver(sf);
        this.addKeyListener(new KeyActionGame(g));
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

    @Override
    public void update(Observable o, Object arg) {
        if ((Boolean) arg ){
            try {
                File soundFile = new File("wav/pacman_death.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}
