package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.client.masks.RoundedRectangularMask;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Greg on 8/23/2014.
 * An on-screen dialog box that can be added to a ScreenArea and rendered like any other graphic.
 */
public class DialogBox extends ScreenArea {

    private ScreenArea container;
    private ArrayList<DialogItem> items;

    public DialogBox(Dimension size, ScreenArea container) {
        super(size, new Point(), new Point(), 0);
        this.container = container;
    }

    public Point getPosition() {
        Dimension screenSize = container.getSize();
        return new Point(screenSize.width / 2 - size.width / 2, screenSize.height / 2 - size.height / 2);
    }

    public void resize(Dimension size) {
        this.size = size;
        int smallerSide = (size.width > size.height) ? size.width : size.height;
        Dimension cornerSize = new Dimension(smallerSide / 5, smallerSide / 5);
        this.graphic = new Graphic(new RoundedRectangularMask(size, cornerSize));
        this.needsRendering = true;
    }
}
