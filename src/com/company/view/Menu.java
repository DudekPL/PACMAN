package com.company.view;

import javax.swing.*;
import java.awt.*;

public class Menu extends JDesktopPane{
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        String path;
        long t = System.currentTimeMillis() % 800;
        if (t>400) path = "img/Menu/menu1.png";
        else path = "img/Menu/menu2.png";
        ImageIcon img = new ImageIcon(path);
        g2.scale(720/((float)img.getIconWidth()), 360/((float)img.getIconHeight()));
        g2.drawImage(img.getImage(), 0,0 ,null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720, 360);
    }
}