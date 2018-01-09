package com.company.control;


import com.company.model.FieldModel;
import com.company.util.enums.Inside;

public class FieldController {
    private FieldModel model;

    public FieldController(FieldModel m) {model = m;}
    Inside eat() {
        Inside pom = model.getInside();
        model.setInside(Inside.EMPTY);
        return pom;
    }
}