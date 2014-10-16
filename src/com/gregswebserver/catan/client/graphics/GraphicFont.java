package com.gregswebserver.catan.client.graphics;

import java.awt.*;

/**
 * Created by Greg on 8/24/2014.
 * A font that is able to be rendered to the screen using TextGraphic.
 */
public class GraphicFont {

    private Graphic[] chars;
    private Point[] positions;

    public GraphicFont(GraphicSource source) {
        positions = new Point[256];
        chars = new Graphic[256];
    }
}
