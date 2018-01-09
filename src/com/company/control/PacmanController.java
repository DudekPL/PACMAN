package com.company.control;


import com.company.model.PacmanModel;
import com.company.util.enums.Direction;
import com.company.view.PacmanSimpleView;
import com.company.wrapper.Map;

public class PacmanController extends GhostController {
    public PacmanController(PacmanModel pm, PacmanSimpleView v, Map m, int rx, int ry) {
        super(pm, v, m, rx, ry);
    }

    @Override
    public void respawn() {
        synchronized (model) {
            ((PacmanModel)model).setNextmove(Direction.NONE);
            super.respawn();
        }
    }

    public void move() {
        synchronized (model) {
            Direction d = ((PacmanModel)model).getNextmove();
            int x = model.getPosx();
            int y = model.getPosy();
            if (d == Direction.RIGHT && map.model.field(x,y).model.canRight()) {model.changePos(d); return;}
            if (d == Direction.LEFT && map.model.field(x,y).model.canLeft()) {model.changePos(d); return;}
            if (d == Direction.DOWN && map.model.field(x,y).model.canDown()) {model.changePos(d); return;}
            if (d == Direction.UP && map.model.field(x,y).model.canUp()) {model.changePos(d); return;}
        }
    }

    public void setnextMove(Direction d){
        int x = model.getPosx();
        int y = model.getPosy();
        if (d == Direction.RIGHT && !map.model.field(x,y).model.canRight()) return;
        if (d == Direction.LEFT && !map.model.field(x,y).model.canLeft()) return;
        if (d == Direction.DOWN && !map.model.field(x,y).model.canDown()) return;
        if (d == Direction.UP && !map.model.field(x,y).model.canUp()) return;
        ((PacmanModel)model).setNextmove(d);
    }
}