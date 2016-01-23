package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by Greg on 1/18/2015.
 * A graphic with an underlying BufferedImage
 */
public class BufferedGraphic extends Graphic {

    private final BufferedImage image;

    public BufferedGraphic(RenderMask mask) {
        this(new BufferedImage(mask.getWidth(), mask.getHeight(), TYPE_INT_RGB));
    }

    public BufferedGraphic(BufferedImage image) {
        this.image = image;
        setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        setMask(new RectangularMask(new Dimension(image.getWidth(), image.getHeight())));
        clear();
    }

    public Graphics getDrawGraphics() { return image.getGraphics(); }
}
