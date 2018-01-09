package com.company;

public class FieldController {
    private FieldModel model;

    public FieldController(FieldModel m) {model = m;}
    public Inside eat() {
        Inside pom = model.getInside();
        model.setInside(Inside.EMPTY);
        return pom;
    }
}