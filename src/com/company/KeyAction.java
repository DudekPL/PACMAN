package com.company;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyAction implements KeyListener{
    private final Game game;
    private final int left;
    private final int right;
    private final int up;
    private final int down;
    private final int pause;

    public KeyAction(Game game, int left, int right, int up, int down, int pause) {
        this.game = game;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.pause = pause;
    }

    public KeyAction(Game game) {
        this.game = game;
        this.left = KeyEvent.VK_A;
        this.right = KeyEvent.VK_D;
        this.up = KeyEvent.VK_W;
        this.down = KeyEvent.VK_S;
        this.pause = KeyEvent.VK_P;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == pause) game.controller.pause();
        if (game.model.isPaused()) return;
        PacmanController pc = game.model.player.controller;
        if(key == right) pc.setnextMove(Direction.RIGHT);
        if(key == left) pc.setnextMove(Direction.LEFT);
        if(key == down) pc.setnextMove(Direction.DOWN);
        if(key == up) pc.setnextMove(Direction.UP);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
