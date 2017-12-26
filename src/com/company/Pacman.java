package com.company;

class PacmanModel extends GhostModel{
    private Direction nextmove;

    public PacmanModel(int x, int y) {
        super(x, y);
        nextmove = Direction.NONE;
    }
    public int getPosx() {return posx;}
    public int getPosy() {return posy;}
    public void changePos (Direction d){
        switch (d) {
            case UP:    this.posy=posy-1;
                break;
            case DOWN:  posy=posy+1;
                break;
            case LEFT:  posx=posx-1;
                break;
            case RIGHT: posx=posx+1;
                break;
        }
}

public class Pacman {
    private PacmanModel model;
        }