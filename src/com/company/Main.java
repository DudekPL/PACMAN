package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) { //TODO zeby animacja ruchu sie koczyla przy smierci i menu glowne z zapisem leaderboard
        //TODO dodac guzik nowa gra i wycisz na planszy
        EventQueue.invokeLater(() -> {
            Game g = new Game(800, 5000, 5000);
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
