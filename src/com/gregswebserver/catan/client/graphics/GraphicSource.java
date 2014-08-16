package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.log.LogLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Greg on 8/14/2014.
 * Class for importing images to be used for sprites.
 */
public class GraphicSource extends Graphic {

    private GraphicSource(int width, int height, int[] pixels) {
        super(pixels, new RectangularMask(width, height));
    }

    public static GraphicSource load(String path) {
        int width = 0, height = 0;
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            Main.logger.log("Error loading image source at " + path, e, LogLevel.ERROR);
        }
        return new GraphicSource(width, height, pixels);
    }
}
