package com.company;


public class Map {
    public MapModel model;
    public MapSimpleView view;
    public MapController controller;

    public Map() {
        model = new MapModel();
        view = new MapSimpleView(model);
        controller = new MapController(model);
    }
}