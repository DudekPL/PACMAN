package com.company.view;

import com.company.model.MapModel;

import javax.swing.*;
import java.awt.*;

public class MapSimpleView extends JLayeredPane {
    public static final int FIELD_SIZE = 40;
    private MapModel model;

    public MapSimpleView(MapModel m) {
        model = m;
        setLayout(new GridLayout(model.getSizey(), model.getSizex()));
        for (int i = 0; i < model.getSizey(); i++)
            for (int j = 0; j < model.getSizex(); j++) {
                add(model.field(j, i).view);
                model.field(j, i).view.setFocusable(false);
            }
        setOpaque(false);
        setFocusable(false);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}