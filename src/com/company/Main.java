package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Game g = new Game();
            JFrame fr = new JFrame();
            //fr.add(g.model.map.view);
            fr.add(g.view);
            fr.pack();
            fr.setVisible(true);
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setTitle("PAC-MAN");
            g.model.ghosts.get(0).model.changePos(Direction.UP);
            Timer t = new Timer(2, event -> {
                g.view.repaint();
            });
            t.start();
        });
    }
}
