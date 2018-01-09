package com.company.control;


import com.company.model.GameModel;
import com.company.util.enums.Inside;
import com.company.util.enums.State;
import com.company.wrapper.Ghost;

public class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }
    public synchronized void eat() {
        Inside i = model.map.controller.eat(model.player.model.getPosx(), model.player.model.getPosy());
        switch (i) {
            case EMPTY:
                break;
            case DOT: model.addPoints(10);
                break;
            case BIGDOT: model.addPoints(10);
                for (Ghost g: model.ghosts) {
                    javax.swing.Timer t = new javax.swing.Timer(0, event->g.controller.makeEatable());
                    t.setInitialDelay((int)model.timeformove);
                    t.setRepeats(false);
                    t.start();
                }
                break;
            case FRUIT: model.addPoints(50);
        }
    }
    public synchronized void collide(int i, State s){
        if (s == State.RESPAWNING) return;
        if (s == State.CHASING) {
            model.player.controller.collided();
            model.lostLive();
        }
        if (s == State.EATABLE) {
            model.addPoints(100);
            model.ghosts.get(i).controller.respawn();
        }
        model.ghosts.get(i).controller.collided();
    }
}
