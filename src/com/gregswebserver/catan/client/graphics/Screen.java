package com.gregswebserver.catan.client.graphics;


import com.gregswebserver.catan.client.renderer.ScreenObject;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Greg on 8/15/2014.
 * Screen class for printing an image onto the ClientWindow.
 */
public class Screen extends Graphic {

    private Canvas canvas;
    private BufferStrategy buffer;
    private Graphics graphics;
    private BufferedImage image;
    private HashMap<Coordinate, ScreenObject> hitboxMap;
    private HashSet<ScreenObject> renderBuffer;

    public Screen(int width, int height, double scale) {
        setMask(new RectangularMask((int) (width * scale), (int) (height * scale)));
        canvas = new Canvas() {
            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }
        };
        canvas.createBufferStrategy(3);
        buffer = canvas.getBufferStrategy();
        graphics = buffer.getDrawGraphics();
    }

    public void render() {
        for (ScreenObject object : renderBuffer) {
            object.getGraphic().renderTo(this, object.getPosition());
        }
    }

    public void show() {
        graphics.dispose();
        buffer.show();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public HashMap<Coordinate, ScreenObject> getHitboxMap() {
        return hitboxMap;
    }
}
