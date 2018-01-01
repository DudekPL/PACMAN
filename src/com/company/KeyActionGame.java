package com.company;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyActionGame implements KeyListener{
    private final Game game;
    private final int left;
    private final int right;
    private final int up;
    private final int down;

    public KeyActionGame(Game game, int left, int right, int up, int down) {
        this.game = game;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    public KeyActionGame(Game game) {
        this.game = game;
        this.left = KeyEvent.VK_A;
        this.right = KeyEvent.VK_D;
        this.up = KeyEvent.VK_W;
        this.down = KeyEvent.VK_S;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        PacmanController pc = game.model.player.controller;
        if(key == right) pc.setnextMove(Direction.RIGHT);
        if(key == left) pc.setnextMove(Direction.LEFT);
        if(key == down) pc.setnextMove(Direction.DOWN);
        if(key == up) pc.setnextMove(Direction.UP);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
