package com.company;

public class MapController {
    private MapModel model;

    public MapController(MapModel m) {model = m;}
    public Inside eat(int x, int y, int delay) {return model.field(x, y).controller.eat();}
}
