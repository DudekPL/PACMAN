package com.company.wrapper;


import com.company.control.MapController;
import com.company.model.MapModel;
import com.company.view.MapSimpleView;

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