package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

class MapModel {
    private int sizex;
    private int sizey;
    private Field[][] fields;


    public MapModel(){
        sizex=18;
        sizey=9;
        fields= new Field[18][9];
        for (int i = 0; i < 18; i++) for (int j = 0; j < 9; j++) fields[i][j] = new Field();
        for (Field f:fields[0]) f.setLeft(false);
        for (Field f:fields[17]) f.setRight(false);
        for (int i = 0; i <18; i++) {
            fields[i][0].setUp(false);
            fields[i][8].setDown(false);
        }
        rectHorS(3,3);
        rectHorS(13,3);
        rectHorS(3, 5);
        rectHorS(13,5);
        rectVertU(4,0);
        rectVertU(13,0);
        rectVerD(4,7);
        rectVerD(13, 7);
        rectHorB(6, 1);
        rectHorB(6, 7);
        //"elki":
        rectHorS(1, 1);//lewa gora
        rectVertU(1, 2);
        fields[1][1].setDown(true);
        fields[1][2].setUp(true);
        rectHorS(15,1);//prawa gora
        rectVertU(16, 2);
        fields[16][1].setDown(true);
        fields[16][2].setUp(true);
        rectHorS(1, 7);//lewa dol
        rectVerD(1, 5);
        fields[1][7].setUp(true);
        fields[1][6].setDown(true);
        rectHorS(15, 7);//prawa dol
        rectVerD(16, 5);
        fields[16][7].setUp(true);
        fields[16][6].setDown(true);
        respBox(6, 3);
        fields[0][0].setInside(Inside.BIGDOT);
        fields[17][8].setInside(Inside.BIGDOT);
    }
    public int getSizex() {return sizex;}
    public int getSizey() {return sizey;}
    public Field field(int x, int y) {return fields[x][y];}
    private void rectHorS(int x, int y){
        fields[x][y].setInside(Inside.EMPTY);
        fields[x+1][y].setInside(Inside.EMPTY);
        fields[x][y].setDown(false);
        fields[x][y].setDown(false);
        fields[x][y].setUp(false);
        fields[x][y].setLeft(false);
        fields[x+1][y].setDown(false);
        fields[x+1][y].setUp(false);
        fields[x+1][y].setRight(false);
        fields[x-1][y].setRight(false);
        fields[x+2][y].setLeft(false);
        fields[x][y-1].setDown(false);
        fields[x][y+1].setUp(false);
        fields[x+1][y-1].setDown(false);
        fields[x+1][y+1].setUp(false);
    }
    private void rectHorB(int x, int y){
        for (int i = 0; i <6 ; i++) {
            fields[x+i][y].setInside(Inside.EMPTY);
            fields[x+i][y].setUp(false);
            fields[x+i][y].setDown(false);
            fields[x+i][y-1].setDown(false);
            fields[x+i][y+1].setUp(false);
        }
        fields[x][y].setLeft(false);
        fields[x-1][y].setRight(false);
        fields[x+5][y].setRight(false);
        fields[x+6][y].setLeft(false);
    }
    private void rectVertU(int x, int y){
        fields[x][y].setInside(Inside.EMPTY);
        fields[x][y+1].setInside(Inside.EMPTY);
        fields[x][y].setLeft(false);
        fields[x][y].setRight(false);
        fields[x][y+1].setLeft(false);
        fields[x][y+1].setRight(false);
        fields[x][y+1].setDown(false);
        fields[x-1][y].setRight(false);
        fields[x+1][y].setLeft(false);
        fields[x-1][y+1].setRight(false);
        fields[x+1][y+1].setLeft(false);
        fields[x][y+2].setUp(false);
    }
    private void rectVerD(int x, int y){
        fields[x][y].setInside(Inside.EMPTY);
        fields[x][y+1].setInside(Inside.EMPTY);
        fields[x][y].setLeft(false);
        fields[x][y].setRight(false);
        fields[x][y].setUp(false);
        fields[x][y+1].setLeft(false);
        fields[x][y+1].setRight(false);
        fields[x-1][y].setRight(false);
        fields[x+1][y].setLeft(false);
        fields[x-1][y+1].setRight(false);
        fields[x+1][y+1].setLeft(false);
        fields[x][y-1].setDown(false);
    }
    private void respBox(int x, int y){
        for (int i = 0; i < 6; i++) {
            fields[x+i][y].setInside(Inside.EMPTY);
            fields[x+i][y].setUp(false);
            fields[x+i][y-1].setDown(false);
            fields[x+i][y+2].setInside(Inside.EMPTY);
            fields[x+i][y+2].setDown(false);
            fields[x+i][y+3].setUp(false);
            fields[x+i][y+1].setInside(Inside.EMPTY);
        }
        for (int i = 0; i < 3; i++) {
            fields[x][y+i].setLeft(false);
            fields[x-1][y+i].setRight(false);
            fields[x+5][y+i].setRight(false);
            fields[x+6][y+i].setLeft(false);
        }
        fields[x+2][y].setUp(true);
        fields[x+3][y].setUp(true);
    }
}

class MapSimpleView extends JComponent{
    private MapModel model;
    public static final int FIELD_SIZE = 40;
    private static final int WALL_WIDTH = 5;

    public MapSimpleView(MapModel m) {
        model=m;
    }
    public void paintComponent(Graphics g) {
        int r =WALL_WIDTH/2;
        Rectangle2D field;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < model.getSizex() ; i++) {
            for (int j = 0; j < model.getSizey(); j++) {
                g2.setPaint(Color.BLACK);
                field = new Rectangle2D.Float(i*FIELD_SIZE, j*FIELD_SIZE, FIELD_SIZE, FIELD_SIZE);
                g2.fill(field);
                g2.setPaint(Color.BLUE);
                if (!model.field(i,j).canUp()) g2.fill(new Rectangle2D.Float(i*FIELD_SIZE,j*FIELD_SIZE, FIELD_SIZE, WALL_WIDTH));
                if (!model.field(i,j).canDown()) g2.fill(new Rectangle2D.Float(i*FIELD_SIZE,(j+1)*FIELD_SIZE-WALL_WIDTH, FIELD_SIZE, WALL_WIDTH));
                if (!model.field(i,j).canLeft()) g2.fill(new Rectangle2D.Float(i*FIELD_SIZE,j*FIELD_SIZE, WALL_WIDTH, FIELD_SIZE));
                if (!model.field(i,j).canRight()) g2.fill(new Rectangle2D.Float((i+1)*FIELD_SIZE-WALL_WIDTH,j*FIELD_SIZE, WALL_WIDTH, FIELD_SIZE));
                g2.setPaint(Color.WHITE);
                if (model.field(i,j).getInside() == Inside.DOT) g2.fill(new Ellipse2D.Float((float)(i+0.5)*FIELD_SIZE-r, (float)(j+0.5)*FIELD_SIZE-r, 2*r, 2*r));
                if (model.field(i,j).getInside() == Inside.BIGDOT) g2.fill(new Ellipse2D.Float((float)(i+0.5)*FIELD_SIZE-2*r, (float)(j+0.5)*FIELD_SIZE-2*r, 4*r, 4*r));
                g2.setPaint(Color.RED);
                if (model.field(i,j).getInside() == Inside.FRUIT) g2.fill(new Ellipse2D.Float((float)(i+0.5)*FIELD_SIZE-2*r, (float)(j+0.5)*FIELD_SIZE-2*r, 4*r, 4*r));
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(model.getSizex()*FIELD_SIZE, model.getSizey()*FIELD_SIZE);
    }
}

public class Map{
    public MapModel model;
    public MapSimpleView view;

    public Map(){
        model = new MapModel();
        view = new MapSimpleView(model);
    }
}