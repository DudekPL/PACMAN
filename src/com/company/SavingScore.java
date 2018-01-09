package com.company;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SavingScore {
    private static int yourscore = 0;

    public static void upload(int score){
        yourscore = score;
    }

    public static void save(){
        EndFlag.saving = true;
        Queue<String> inside = new LinkedList<>();
        Scanner boardreader = null;
        PrintWriter boardwriter = null;
        int i = 0;
        boolean saved = false;
        try{
            File board = new File("lboard.txt");
            if (board.createNewFile()) {
                boardwriter = new PrintWriter(board);
            }
            boardreader = new Scanner(board);
            while (boardreader.hasNextLine() && i<9) {
                String name = boardreader.nextLine();
                int points = Integer.parseInt(boardreader.nextLine());
                if (yourscore>points && !saved){
                    String playername = "aaaaaa";
                    while(playername.length()>5){
                        playername = JOptionPane.showInputDialog(null, "Wpisz swoje imię\n(MAX 5 znaków)", "", JOptionPane.QUESTION_MESSAGE);
                    }
                    inside.offer(playername);
                    inside.offer(Integer.toString(yourscore));
                    i+=1;
                    saved = true;
                }
                if(i>=9) break;
                inside.offer(name);
                inside.offer(Integer.toString(points));
                i+=1;
            }
            board.delete();
            if (!board.createNewFile()){
                board.delete();
                while(board.createNewFile()){}
            }
            boardwriter = new PrintWriter(board);
            if(i==0) {
                String playername = "aaaaaa";
                while(playername.length()>5){
                    playername = JOptionPane.showInputDialog(null, "Wpisz swoje imię\n(MAX 5 znaków)");
                }
                inside.offer(playername);
                inside.offer(Integer.toString(yourscore));
            }
            String help;
            while (!inside.isEmpty()){
                help = inside.poll();
                if (inside.isEmpty()) break;
                boardwriter.println(help);
                boardwriter.println(inside.poll());
            }
            //***************READING***************************//
            StringBuilder message = new StringBuilder();
            board = new File("lboard.txt");
            if (board.createNewFile()) JOptionPane.showMessageDialog(null, "Plik został uszkodzony lub usunięty.\nStworzono nowy.", "Leaderboard", JOptionPane.PLAIN_MESSAGE);
            boardreader.close();
            boardwriter.close();
            boardreader = new Scanner(board);
            i = 1;
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
            JOptionPane.showMessageDialog(null, "Błąd odczytu tabeli wyników\nNie można zapisać twojego wyniku", "Leaderboard", JOptionPane.ERROR_MESSAGE);
        }
        catch (SecurityException exc){
            JOptionPane.showMessageDialog(null, "Plik tabeli wyników jest chroniony.\nNie można zapisać twojego wyniku", "Leaderboard", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException exc) {
        }
        finally {
            if (boardreader != null) boardreader.close();
            if (boardwriter != null) boardwriter.close();
        }
        return;
    }
}
