package com.company.util.tasks;

import com.company.model.GameModel;
import com.company.util.EndFlag;
import com.company.wrapper.Ghost;

import java.util.TimerTask;

public class  UpdateTask extends TimerTask {
    private GameModel gm;

    public UpdateTask(GameModel m) {
        gm = m;
    }

    @Override
    public void run() {
        if (!EndFlag.ending) {
            for (Ghost g: gm.ghosts) {
                g.controller.setActPos();
            }
            gm.player.controller.setActPos();
        }
        if (EndFlag.ending) {
            EndFlag.updating = true;
            cancel();
        }
    }
}