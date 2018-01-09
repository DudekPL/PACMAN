package com.company.wrapper;


import com.company.control.GhostController;
import com.company.model.GhostModel;
import com.company.view.GhostSimpleView;

import java.awt.*;

public class Ghost {
    public GhostModel model;
    public GhostSimpleView view;
    public GhostController controller;

    public Ghost(Map m, int x, int y, Color c, int size, long tfm, long tfe, long tfr) {
        model = new GhostModel(x, y, tfe, tfr, c);
        view = new GhostSimpleView(model, c, size, tfm);
        controller = new GhostController(model, view, m, x, y);
        model.addObserver(view);
    }
}