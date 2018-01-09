package com.company.view;

import com.company.model.GhostModel;
import com.company.util.enums.Direction;
import com.company.util.enums.State;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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


    public int getActposx() {
        return actposx;
    }

    public int getActposy() {
        return actposy;
    }

    static private String getPath(Color c) {
        String p = "img/Ghosts/Pinky/";
        if (c == Color.RED) p = "img/Ghosts/Blinky/";
        if (c == Color.BLUE) p = "img/Ghosts/Inky/";
        if (c == Color.YELLOW || c == Color.ORANGE) p = "img/Ghosts/Clyde/";
        return p;
    }

    protected void display() {
        float time = System.currentTimeMillis() - timeofchange;
        float atimeformove = timeformove;
        if (model.getStatus() == State.EATABLE) atimeformove *=1.7;
        if (time > atimeformove) time = atimeformove;
        float posx = (float) fieldsize * (float) (model.getPosx() - prevposx) * (time /atimeformove);
        actposx = (int)posx + prevposx * fieldsize;
        float posy = (float) fieldsize * (float) (model.getPosy() - prevposy) * (time / atimeformove);
        actposy = (int)posy + prevposy * fieldsize;
        setLocation(actposx, actposy);
    }

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
            if(System.currentTimeMillis() - timeofeating > 0.6*model.timeforeating) {
                long t = System.currentTimeMillis() % 800;
                if (t>400) img = new ImageIcon("img/Ghosts/Eatable/2.png");
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
            if (obj instanceof Boolean) {
                if ((Boolean)obj){
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
                    return;
                }
            }
            timeofchange = System.currentTimeMillis();
            if(((GhostModel)obs).getStatus() != State.EATABLE)  timeofeating = 0;
            else if (timeofeating == 0) timeofeating = System.currentTimeMillis();
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