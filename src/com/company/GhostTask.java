package com.company;

import java.util.TimerTask;

public class GhostTask extends TimerTask {
    private Game gm;
    private State state;
    private int i;

    public GhostTask(Game g, State s){
        super();
        gm = g;
        state = s;
    }

    @Override
    public void run(){
        if (!gm.model.isPaused())
            i = 0;
        for (Ghost g: gm.model.ghosts) {
            synchronized (g.model) {
                if (g.model.getStatus() == state) {
                    if (Math.abs(g.model.getActposx() - gm.model.player.model.getActposx()) < (38) &&
                            Math.abs(g.model.getActposy() - gm.model.player.model.getActposy()) < (38) ){
                        gm.controller.collide(i, g.model.getStatus());
                    }
                    g.controller.move(gm.model.player.model.getPosx(), gm.model.player.model.getPosy());
                }
                i +=1;
            }
        }
        if (EndFlag.ending){
            switch (state) {
                case RESPAWNING:
                    EndFlag.resping = true;
                    break;
                case EATABLE:
                    EndFlag.eating = true;
                    break;
                case CHASING:
                    EndFlag.chasing = true;
                    break;
            }
            cancel();
        }
    }
}