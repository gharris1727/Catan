package com.gregswebserver.catan.client.graphics;


import com.gregswebserver.catan.client.masks.RectangularMask;

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
    private BufferedImage image;

    public Screen() {
    }

    public void show() {
        if (canvas != null && canvas.isValid()) {
            if (buffer == null) {
                canvas.createBufferStrategy(3);
                buffer = canvas.getBufferStrategy();
            }
            Graphics graphics = buffer.getDrawGraphics();
            graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
            graphics.dispose();
            buffer.show();
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setSize(Dimension size) {
        //Configure the underlying Graphic superclass.
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        image = gc.createCompatibleImage(size.width, size.height);
        setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        setMask(new RectangularMask(size));
        buffer = null; //MUST INVALIDATE THE OLD BUFFER.
        canvas = new ScreenCanvas(size);
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
            hitbox[i] = 0;
        }
    }

    public class ScreenCanvas extends Canvas {

        private final Dimension size;

        public ScreenCanvas(Dimension size) {
            this.size = size;
        }

        public int getWidth() {
            return size.width;
        }

        public int getHeight() {
            return size.height;
        }

        public String toString() {
            return "ScreenCanvas";
        }

    }
}
