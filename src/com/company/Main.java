package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            Game g = new Game();
            JFrame fr = new JFrame();
            fr.add(g);
            fr.pack();
            fr.setVisible(true);
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setTitle("PAC-MAN");
            g.ghosts.get(0).model.changePos(Direction.UP);
            Timer t = new Timer(16, event-> {
                g.repaint();
            });
            t.start();
        });
    }
}
