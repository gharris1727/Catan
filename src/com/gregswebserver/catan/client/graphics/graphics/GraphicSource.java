package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.ExternalResource;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by Greg on 8/14/2014.
 * Class for importing images to be used for sprites.
 */
public class GraphicSource extends Graphic {

    public GraphicSource(String path) throws IOException {
        BufferedImage image = ImageIO.read(ExternalResource.class.getResourceAsStream(path));
        int width = image.getWidth(), height = image.getHeight();
        int pixelsRGBA[] = new int[width * height];
        image.getRGB(0, 0, width, height, pixelsRGBA, 0, width);
        init(new RectangularMask(new Dimension(width,height)), new BufferedImage(width, height, TYPE_INT_RGB));
        loadRaster();
        for (int i = 0; i < width * height; i++) {
            int r = (pixelsRGBA[i] & 0xff0000) >> 16;
            int g = (pixelsRGBA[i] & 0xff00) >> 8;
            int b = (pixelsRGBA[i] & 0xff);
            pixels[i] = r << 16 | g << 8 | b;
        }
    }
}
