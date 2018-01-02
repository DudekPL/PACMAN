package com.company;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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
            StringBuilder message = new StringBuilder();
            Scanner boardreader = null;
            try{
                File board = new File("lboard.txt");
                if (board.createNewFile()) JOptionPane.showMessageDialog(null, "Plik został uszkodzony lub usunięty.\nStworzono nowy.", "Leaderboard", JOptionPane.PLAIN_MESSAGE);
                boardreader = new Scanner(board);
                if (boardreader.hasNextInt()){
                    boardreader.nextLine();
                }
                int i = 1;
                while (boardreader.hasNextLine()) {
                    String name = boardreader.nextLine();
                    String points = boardreader.nextLine();
                    message.append(Integer.toString(i));
                    message.append(". ");
                    message.append(name);
                    for (int j = 0; j < 21-name.length()-3-points.length(); j++) {
                        message.append("_");
                    }
                    message.append(points);
                    message.append("\n");
                    i+=1;
                }
                JOptionPane.showMessageDialog(null, message.toString(), "Leaderboard", JOptionPane.PLAIN_MESSAGE);
            }
            catch (IOException exc){
                JOptionPane.showMessageDialog(null, "Błąd odczytu", "Leaderboard", JOptionPane.ERROR_MESSAGE);
            }
            catch (SecurityException exc){
                JOptionPane.showMessageDialog(null, "Plik jest chroniony", "Leaderboard", JOptionPane.ERROR_MESSAGE);
            }
            finally {
                if (boardreader != null) boardreader.close();
            }
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