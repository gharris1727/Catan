package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by Greg on 1/18/2015.
 * A graphic with an underlying BufferedImage
 */
public class BufferedGraphic extends Graphic {

    private BufferedImage image;

    public BufferedGraphic(BufferedImage image) {
        this.image = image;
        setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        setMask(new RectangularMask(new Dimension(image.getWidth(), image.getHeight())));
    }

    @Override
    public void clear() {
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = clickable[i] = 0;
    }
}
