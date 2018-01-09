package com.company.control;


import com.company.model.GameModel;
import com.company.util.enums.Inside;
import com.company.util.enums.State;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }
    public void pause() {
        model.setPaused(!model.isPaused());
    }
    public synchronized void eat() {
        Inside i = model.map.controller.eat(model.player.model.getPosx(), model.player.model.getPosy(), (int)model.timeformove);
        switch (i) {
            case DOT: model.addPoints(10);
                break;
            case BIGDOT: model.addPoints(10);
                for (Ghost g: model.ghosts) {
                    javax.swing.Timer t = new javax.swing.Timer(0, event->g.controller.makeEatable());
                    t.setInitialDelay((int)model.timeformove);
                    t.setRepeats(false);
                    t.start();
                }
                break;
            case FRUIT: model.addPoints(50);
        }
        if(i != Inside.EMPTY) {
            javax.swing.Timer t = new javax.swing.Timer(0, event->{
                try {
                    File soundFile = new File("wav/pacman_eatfruit.wav");
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
            });
            t.setInitialDelay((int)model.timeformove);
            t.setRepeats(false);
            t.start();
        }
    }
    public synchronized void collide(int i, State s){
        if (s == State.CHASING) {
            model.player.controller.collided();
            model.lostLive();
        }
        if (s == State.EATABLE) {
            model.addPoints(100);
            try {
                File soundFile = new File("wav/pacman_eatghost.wav");
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
            model.ghosts.get(i).controller.respawn();
        }
        model.ghosts.get(i).controller.collided();
    }
}
