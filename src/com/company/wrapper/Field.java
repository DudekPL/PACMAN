package com.company.wrapper;


import com.company.control.FieldController;
import com.company.model.FieldModel;
import com.company.view.FieldSimpleView;

public class Field {
    public FieldModel model;
    public FieldSimpleView view;
    public FieldController controller;

    public Field() {
        model = new FieldModel();
        view = new FieldSimpleView(model);
        controller = new FieldController(model);
        model.addObserver(view);
    }
}