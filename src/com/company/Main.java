package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Game g = new Game();
            JFrame fr = new JFrame();
            fr.add(g.view);
            fr.pack();
            fr.setVisible(true);
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setTitle("PAC-MAN");
            fr.addKeyListener(new KeyAction(g));
            g.model.ghosts.get(0).model.changePos(Direction.UP);
            Timer t = new Timer(20, event -> {
                g.view.repaint();
                g.model.addPoints(1);
            });
            Timer t2 = new Timer(1000, event -> g.model.player.controller.move());
            t.start();
            t2.start();
        });
    }
}
