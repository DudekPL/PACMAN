package com.company.control;


import com.company.model.MapModel;
import com.company.util.enums.Inside;

public class MapController {
    private MapModel model;

    public MapController(MapModel m) {model = m;}
    public Inside eat(int x, int y, int delay) {return model.field(x, y).controller.eat();}
}
