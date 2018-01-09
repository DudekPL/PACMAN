package com.company.util.tasks;

import com.company.util.EndFlag;
import com.company.wrapper.Game;
import com.company.wrapper.Ghost;

import java.util.TimerTask;

public class PlayerTask extends TimerTask {
    private Game gm;
    private int delay;

    public PlayerTask(Game g, int delay) {
        super();
        gm = g;
        this.delay = delay;
    }

    @Override
    public void run() {
        if (!gm.model.isPaused()) {
            gm.model.player.controller.move();
            gm.controller.eat();
            if (gm.model.map.model.isEmpty()) {
                javax.swing.Timer t = new javax.swing.Timer(0, event->{
                    gm.model.map.model.init();
                    for (Ghost g : gm.model.ghosts) {
                        g.controller.respawn();
                    }
                    gm.model.player.controller.respawn();
                });
                t.setInitialDelay(delay);
                t.setRepeats(false);
                t.start();
            }
        }
        if (EndFlag.ending){
            EndFlag.pacman = true;
            cancel();
        }
    }
}