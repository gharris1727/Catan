package com.gregswebserver.catan.client.graphics.util;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.common.log.LogLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Greg on 8/14/2014.
 * Class for importing images to be used for sprites.
 */
public class GraphicSource extends Graphic {

    private GraphicSource(int width, int height, int[] pixels, String path) {
        super(pixels, new int[width * height], new RectangularMask(new Dimension(width, height)));
        name = path;
    }

    public static GraphicSource load(String path) {
        int width = 0, height = 0;
        int[] pixelsRGBA = null, pixels = null;
        try {
            BufferedImage image = ImageIO.read(Main.class.getResourceAsStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixelsRGBA = new int[width * height];
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixelsRGBA, 0, width);
            pixels = new int[width * height];
            for (int i = 0; i < width * height; i++) {
                int r = (pixelsRGBA[i] & 0xff0000) >> 16;
                int g = (pixelsRGBA[i] & 0xff00) >> 8;
                int b = (pixelsRGBA[i] & 0xff);
                pixels[i] = r << 16 | g << 8 | b;
            }
        } catch (Exception e) {
            Main.logger.log("Error loading image source at " + path, e, LogLevel.ERROR);
        }
        return new GraphicSource(width, height, pixels, path);
    }
}
