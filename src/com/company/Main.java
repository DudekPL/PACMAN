package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Game g = new Game(800, 5000, 5000);
            JFrame fr = new JFrame();
            fr.add(g.view);
            fr.pack();
            fr.setVisible(true);
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setTitle("PAC-MAN");
            fr.addKeyListener(new KeyAction(g));
            Timer t = new Timer(20, event -> {
                g.view.repaint();
            });
            t.start();
            g.run();
        });
    }

    public void demo() {
        EventQueue.invokeLater(() -> {
            Game g = new Game(800, 5000, 2500);
            JFrame fr = new JFrame();
            fr.add(g.view);
            fr.pack();
            fr.setVisible(true);
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setTitle("PAC-MAN");
            fr.addKeyListener(new KeyAction(g));
            g.model.ghosts.get(0).model.changePos(Direction.RIGHT);
            Timer t = new Timer(20, event -> {
                g.view.repaint();
                g.model.addPoints(1);
            });
            Timer t2 = new Timer(800, event -> g.model.player.controller.move());
            t.start();
            t2.start();
        });
    }
}
