package com.company.wrapper;

import com.company.control.PacmanController;
import com.company.model.PacmanModel;
import com.company.view.PacmanSimpleView;

public class Pacman{
    public PacmanModel model;
    public PacmanSimpleView view;
    public PacmanController controller;



    public Pacman(Map m, int x, int y, int size, long tfm) {
        model = new PacmanModel(x, y);
        view = new PacmanSimpleView(model, size, tfm);
        controller = new PacmanController(model, view, m, x, y);
        model.addObserver(view);
    }
}