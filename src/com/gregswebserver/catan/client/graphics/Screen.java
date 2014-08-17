package com.gregswebserver.catan.client.graphics;


import com.gregswebserver.catan.client.renderer.ScreenObject;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by Greg on 8/15/2014.
 * Screen class for printing an image onto the ClientWindow.
 */
public class Screen extends Graphic {

    private Canvas canvas;
    private BufferStrategy buffer;
    private Graphics graphics;
    private BufferedImage image;

    public Screen(int width, int height) {
        //Configure the underlying Graphic superclass.
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        image = gc.createCompatibleImage(width, height);
        setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        setMask(new RectangularMask(width, height));
        canvas = new Canvas() {
            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }
        };
    }

    public void render(ScreenObject object) {
        object.getRenderable().renderTo(this, object.getPosition());
    }

    public void draw() {
        if (buffer == null) {
            canvas.createBufferStrategy(3);
            buffer = canvas.getBufferStrategy();
        }
        graphics = buffer.getDrawGraphics();
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }

    public void show() {
        graphics.dispose();
        buffer.show();
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
