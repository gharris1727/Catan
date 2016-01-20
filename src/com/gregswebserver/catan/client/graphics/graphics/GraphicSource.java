package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.ExternalResource;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.common.resources.ResourceLoadException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Greg on 8/14/2014.
 * Class for importing images to be used for sprites.
 */
public class GraphicSource extends Graphic {

    public GraphicSource(String path) throws IOException {
        int width, height;
        int[] pixelsRGBA, pixels;
        BufferedImage image = ImageIO.read(ExternalResource.class.getResourceAsStream(path));
        width = image.getWidth();
        height = image.getHeight();
        pixelsRGBA = new int[width * height];
        image.getRGB(0, 0, width, height, pixelsRGBA, 0, width);
        pixels = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int r = (pixelsRGBA[i] & 0xff0000) >> 16;
            int g = (pixelsRGBA[i] & 0xff00) >> 8;
            int b = (pixelsRGBA[i] & 0xff);
            pixels[i] = r << 16 | g << 8 | b;
        }
        setPixels(pixels);
        setMask(new RectangularMask(new Dimension(width, height)));
    }
}
