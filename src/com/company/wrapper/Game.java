package com.company.wrapper;

import com.company.control.GameController;
import com.company.model.GameModel;
import com.company.util.EndFlag;
import com.company.util.enums.State;
import com.company.util.tasks.GhostTask;
import com.company.util.tasks.PlayerTask;
import com.company.util.tasks.UpdateTask;
import com.company.view.GameSimpleView;

import java.util.*;

public class Game implements Runnable {
    public GameModel model;
    public GameSimpleView view;
    public GameController controller;
    private long timeformove, timeforresp, timeforeat;

    public Game(long timeformove, long timeforeat, long timeforresp) {
        this.timeforeat = timeforeat;
        this.timeformove = timeformove;
        this.timeforresp = timeforresp;
        init();
    }

    public void init(){
        model = new GameModel(timeformove, timeforeat, timeforresp);
        controller = new GameController(model);
        view = new GameSimpleView(this);
        model.addObserver(view);
        for (int i = 0; i <model.map.model.getSizex() ; i++)
            for (int j = 0; j < model.map.model.getSizey(); j++) model.map.model.field(i,j).view.Timeformove((int)timeformove);
    }

    @Override
    public void run() {
        EndFlag.start();
        Timer chasingtime = new Timer();
        chasingtime.schedule(new GhostTask(this, State.CHASING),0, timeformove);
        Timer eatingtime = new Timer();
        eatingtime.schedule(new GhostTask(this, State.EATABLE),0, (long)(1.7 * timeformove));
        Timer resptime = new Timer();
        resptime.schedule(new GhostTask(this, State.RESPAWNING),0, timeformove);
        Timer pacmantime = new Timer();
        pacmantime.schedule(new PlayerTask(this, (int)timeformove), 0, timeformove);
        Timer updating = new Timer();
        updating.schedule(new UpdateTask(model), 0, 20);
    }
}
