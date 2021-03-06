package com.company.model;

import com.company.util.enums.Inside;
import com.company.wrapper.Field;

public class MapModel {
    private int sizex;
    private int sizey;
    private Field[][] fields;


    public MapModel() {
        sizex = 18;
        sizey = 9;
        fields = new Field[18][9];
        for (int i = 0; i < 18; i++) for (int j = 0; j < 9; j++) fields[i][j] = new Field();
        init();
    }

    public void init(){
        for (int i = 0; i < 18; i++) for (int j = 0; j < 9; j++) fields[i][j].model.setInside(Inside.DOT);
        fields[8][6].model.initInside(Inside.EMPTY);
        for (Field f : fields[0]) f.model.setLeft(false);
        for (Field f : fields[17]) f.model.setRight(false);
        for (int i = 0; i < 18; i++) {
            fields[i][0].model.setUp(false);
            fields[i][8].model.setDown(false);
        }
        rectHorS(3, 3);
        rectHorS(13, 3);
        rectHorS(3, 5);
        rectHorS(13, 5);
        rectVertU(4, 0);
        rectVertU(13, 0);
        rectVerD(4, 7);
        rectVerD(13, 7);
        rectHorB(6, 1);
        rectHorB(6, 7);
        //"elki":
        rectHorS(1, 1);//lewa gora
        rectVertU(1, 2);
        fields[1][1].model.setDown(true);
        fields[1][2].model.setUp(true);
        rectHorS(15, 1);//prawa gora
        rectVertU(16, 2);
        fields[16][1].model.setDown(true);
        fields[16][2].model.setUp(true);
        rectHorS(1, 7);//lewa dol
        rectVerD(1, 5);
        fields[1][7].model.setUp(true);
        fields[1][6].model.setDown(true);
        rectHorS(15, 7);//prawa dol
        rectVerD(16, 5);
        fields[16][7].model.setUp(true);
        fields[16][6].model.setDown(true);
        respBox(6, 3);
        fields[0][0].model.setInside(Inside.BIGDOT);
        fields[17][8].model.setInside(Inside.BIGDOT);
    }

    public int getSizex() {
        return sizex;
    }

    public int getSizey() {
        return sizey;
    }

    public Field field(int x, int y) {
        return fields[x][y];
    }

    private void rectHorS(int x, int y) {
        fields[x][y].model.initInside(Inside.EMPTY);
        fields[x + 1][y].model.initInside(Inside.EMPTY);
        fields[x][y].model.setDown(false);
        fields[x][y].model.setDown(false);
        fields[x][y].model.setUp(false);
        fields[x][y].model.setLeft(false);
        fields[x + 1][y].model.setDown(false);
        fields[x + 1][y].model.setUp(false);
        fields[x + 1][y].model.setRight(false);
        fields[x - 1][y].model.setRight(false);
        fields[x + 2][y].model.setLeft(false);
        fields[x][y - 1].model.setDown(false);
        fields[x][y + 1].model.setUp(false);
        fields[x + 1][y - 1].model.setDown(false);
        fields[x + 1][y + 1].model.setUp(false);
    }

    private void rectHorB(int x, int y) {
        for (int i = 0; i < 6; i++) {
            fields[x + i][y].model.initInside(Inside.EMPTY);
            fields[x + i][y].model.setUp(false);
            fields[x + i][y].model.setDown(false);
            fields[x + i][y - 1].model.setDown(false);
            fields[x + i][y + 1].model.setUp(false);
        }
        fields[x][y].model.setLeft(false);
        fields[x - 1][y].model.setRight(false);
        fields[x + 5][y].model.setRight(false);
        fields[x + 6][y].model.setLeft(false);
    }

    private void rectVertU(int x, int y) {
        fields[x][y].model.initInside(Inside.EMPTY);
        fields[x][y + 1].model.initInside(Inside.EMPTY);
        fields[x][y].model.setLeft(false);
        fields[x][y].model.setRight(false);
        fields[x][y + 1].model.setLeft(false);
        fields[x][y + 1].model.setRight(false);
        fields[x][y + 1].model.setDown(false);
        fields[x - 1][y].model.setRight(false);
        fields[x + 1][y].model.setLeft(false);
        fields[x - 1][y + 1].model.setRight(false);
        fields[x + 1][y + 1].model.setLeft(false);
        fields[x][y + 2].model.setUp(false);
    }

    private void rectVerD(int x, int y) {
        fields[x][y].model.initInside(Inside.EMPTY);
        fields[x][y + 1].model.initInside(Inside.EMPTY);
        fields[x][y].model.setLeft(false);
        fields[x][y].model.setRight(false);
        fields[x][y].model.setUp(false);
        fields[x][y + 1].model.setLeft(false);
        fields[x][y + 1].model.setRight(false);
        fields[x - 1][y].model.setRight(false);
        fields[x + 1][y].model.setLeft(false);
        fields[x - 1][y + 1].model.setRight(false);
        fields[x + 1][y + 1].model.setLeft(false);
        fields[x][y - 1].model.setDown(false);
    }

    private void respBox(int x, int y) {
        for (int i = 0; i < 6; i++) {
            fields[x + i][y].model.initInside(Inside.EMPTY);
            fields[x + i][y].model.setUp(false);
            fields[x + i][y - 1].model.setDown(false);
            fields[x + i][y + 2].model.initInside(Inside.EMPTY);
            fields[x + i][y + 2].model.setDown(false);
            fields[x + i][y + 3].model.setUp(false);
            fields[x + i][y + 1].model.initInside(Inside.EMPTY);
        }
        for (int i = 0; i < 3; i++) {
            fields[x][y + i].model.setLeft(false);
            fields[x - 1][y + i].model.setRight(false);
            fields[x + 5][y + i].model.setRight(false);
            fields[x + 6][y + i].model.setLeft(false);
        }
        fields[x + 2][y].model.setUp(true);
        fields[x + 3][y].model.setUp(true);
    }

    public boolean isEmpty() {
        for (Field[] row:fields) for (Field f: row) if (!f.model.isEmpty()) return false;
        return true;
    }
}
