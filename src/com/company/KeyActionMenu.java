package com.company;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyActionMenu implements KeyListener {
    private final Game game;
    private final JFrame frame;
    private final KeyActionGame key;
    private final Menu menu;

    public KeyActionMenu(Game game, JFrame frame, KeyActionGame key, Menu menu) {
        this.game = game;
        this.frame = frame;
        this.key = key;
        this.menu = menu;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_L) {
            JOptionPane.showMessageDialog(null, "af", "Leaderboard", JOptionPane.PLAIN_MESSAGE);
        }
        if (key == KeyEvent.VK_ENTER) {
            frame.remove(menu);
            game.init();
            frame.add(game.view);
            frame.removeKeyListener(this);
            frame.addKeyListener(this.key);
            frame.revalidate();
            game.run();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}