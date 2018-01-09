package com.company;

import com.company.util.EndFlag;
import com.company.util.SavingScore;
import com.company.util.keys.KeyActionGame;
import com.company.util.keys.KeyActionMenu;
import com.company.wrapper.Game;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Game g = new Game(800, 5000, 6500);
            Menu m = new Menu();
            JFrame fr = new JFrame();
            fr.add(m);
            fr.pack();
            fr.setVisible(true);
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setTitle("PAC-MAN");
            KeyActionGame keygame = new KeyActionGame(g);
            KeyActionMenu keymenu = new KeyActionMenu(g, fr, keygame, m);
            fr.addKeyListener(keymenu);
            EndFlag.init();
            Timer t = new Timer(20, event -> {
                if (EndFlag.isEnded() && !EndFlag.ended) {
                    System.out.println("d");
                    EndFlag.ended = true;
                    fr.remove(g.view);
                    fr.removeKeyListener(keygame);
                    fr.add(m);
                    fr.addKeyListener(keymenu);
                    fr.revalidate();
                    fr.repaint();
                }
                if (EndFlag.ending && !EndFlag.saving) SavingScore.save();
                fr.repaint();
            });
            t.start();
        });
    }
}
