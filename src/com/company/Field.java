package com.company;

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