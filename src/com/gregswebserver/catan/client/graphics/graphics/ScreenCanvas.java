package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.util.Graphical;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by Greg on 1/18/2015.
 * A canvas that has an underlying graphic so that we can render to it.
 */
public class ScreenCanvas extends Canvas implements Graphical {

    private Dimension size;
    private BufferedImage image;
    private BufferedGraphic graphic;
    private BufferStrategy buffer;

    public ScreenCanvas(Dimension size) {
        this.size = size;
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        image = gc.createCompatibleImage(size.width, size.height);
        graphic = new BufferedGraphic(image);
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    public String toString() {
        return "ScreenCanvas";
    }

    @Override
    public Graphic getGraphic() {
        return graphic;
    }

    public synchronized void render() {
        if (isDisplayable()) {
            if (buffer == null) {
                createBufferStrategy(3);
                buffer = getBufferStrategy();
            }
            Graphics graphics = buffer.getDrawGraphics();
            graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            graphics.dispose();
            buffer.show();
        }
    }
}
