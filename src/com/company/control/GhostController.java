package com.company.control;


import com.company.model.GhostModel;
import com.company.util.enums.Direction;
import com.company.util.enums.State;
import com.company.view.GhostSimpleView;
import com.company.wrapper.Map;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class GhostController {
    protected final GhostModel model;
    protected Map map;
    private int respx;
    private int respy;
    protected GhostSimpleView view;

    public GhostController(GhostModel gm, GhostSimpleView v, Map m, int rx, int ry) {
        model = gm;
        map = m;
        respx = rx;
        respy = ry;
        view = v;
    }

    public void setActPos(){
        model.setActPos(view.getActposx(), view.getActposy());
    }

    private Direction nextMoveChasing(int x, int y) {
        int posx,posy;
        posx = model.getPosx();
        posy = model.getPosy();
        return BFS(posx, posy, 7, x, y);
    }

    class Vertex{
        Dimension coords;
        int depth;
        Vertex parent;

        Vertex(int x, int y, int d, Vertex p) {
            coords = new Dimension(x, y);
            depth = d;
            parent = p;
        }

        Vertex(int x, int y) {
            depth = 0;
            coords = new Dimension(x,y);
            parent = null;
        }
    }


    private Direction BFS(int firstx, int firsty, int maxdepth, int pacx, int pacy) {
        Queue<Vertex> q = new LinkedList<>();
        Vertex v = new Vertex(firstx, firsty);
        q.offer(v);
        while(!q.isEmpty()) {
            v = q.poll();
            if (v.coords.width == pacx && v.coords.height == pacy) break;
            if (v.depth<maxdepth) {
                if (map.model.field(v.coords.width, v.coords.height).model.canDown())
                    if (v.parent == null || (v.coords.width != v.parent.coords.width || v.coords.height+1 != v.parent.coords.height))
                        q.offer(new Vertex(v.coords.width, v.coords.height+1,v.depth+1, v));
                if (map.model.field(v.coords.width, v.coords.height).model.canUp())
                    if (v.parent == null || (v.coords.width != v.parent.coords.width || v.coords.height-1 != v.parent.coords.height))
                        q.offer(new Vertex(v.coords.width, v.coords.height-1,v.depth+1, v));
                if (map.model.field(v.coords.width, v.coords.height).model.canLeft())
                    if (v.parent == null || (v.coords.width-1 != v.parent.coords.width || v.coords.height != v.parent.coords.height))
                        q.offer(new Vertex(v.coords.width-1, v.coords.height,v.depth+1, v));
                if (map.model.field(v.coords.width, v.coords.height).model.canRight())
                    if (v.parent == null || (v.coords.width+1 != v.parent.coords.width || v.coords.height != v.parent.coords.height))
                        q.offer(new Vertex(v.coords.width+1, v.coords.height,v.depth+1, v));
            }
        }
        while(v != null && v.parent !=null && (v.parent.coords.width != firstx || v.parent.coords.height != firsty)) {
            v = v.parent;
        }
        if (v == null) return Direction.NONE;
        if (v.coords.height - firsty == 1) return Direction.DOWN;
        if (v.coords.height - firsty == -1) return Direction.UP;
        if (v.coords.width - firstx == 1) return Direction.RIGHT;
        if (v.coords.width - firstx == -1) return Direction.LEFT;
        return Direction.NONE;
    }

    private Direction nextMoveEatable(int x, int y) {
        int posx, posy;
        synchronized (model) {
            posx = model.getPosx();
            posy = model.getPosy();
        }
        Direction d = BFS(posx, posy, 4, x, y);
        if (map.model.field(posx,posy).model.canUp() && d!=Direction.UP) return Direction.UP;
        if (map.model.field(posx,posy).model.canRight() && d!=Direction.RIGHT) return Direction.RIGHT;
        if (map.model.field(posx,posy).model.canLeft() && d!=Direction.LEFT) return Direction.LEFT;
        if (map.model.field(posx,posy).model.canDown() && d!=Direction.DOWN) return Direction.DOWN;
        return Direction.NONE;
    }

    private Direction nextMoveRespawning() {
        int posx, posy, x, y;
        x=y=0;
        synchronized (model) {
            posx = model.getPosx();
            posy = model.getPosy();
        }
        Color c = model.getColor();
        if (c==Color.PINK) {x=17;y=0;}
        if (c==Color.RED) {x=0; y=8;}
        if (c==Color.BLUE) {x=17;y=8;}
        return BFS(posx, posy, 20, x,y);
    }

    public void move(int x, int y) {
        Direction d = Direction.NONE;
        synchronized (model) {
            if (model.getStatus() == State.CHASING) d = nextMoveChasing(x, y);
            if (model.getStatus() == State.EATABLE) d = nextMoveEatable(x, y);
            if (model.getStatus() == State.RESPAWNING) d = nextMoveRespawning();
            model.changePos(d);
        }
    }

    void makeEatable() {
        if (model.getStatus() != State.RESPAWNING) model.makeEatable();
    }

    boolean collided() {
        boolean point = false;
        if (model.getStatus() == State.EATABLE) point = true;
        model.eaten(respx, respy);
        return point;
    }

    public void respawn() {model.respawn(respx, respy);}
}