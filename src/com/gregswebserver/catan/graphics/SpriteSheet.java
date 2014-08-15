package com.gregswebserver.catan.graphics;

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
public class SpriteSheet {

    public static final int FORMAT_RGB = 0x0;
    public static final int FORMAT_RGBA = 0x1;
    private int width, height;
    private int[] pixels, pixelsrgb;

    private SpriteSheet(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public static SpriteSheet load(String path) {
        int width = 0, height = 0;
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            Main.logger.log("Error loading sprite sheet " + path, e, LogLevel.ERROR);
        }
        return new SpriteSheet(width, height, pixels);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels(int format) {
        if (format == FORMAT_RGBA) return pixels;
        if (pixelsrgb != null) return pixelsrgb;
        pixelsrgb = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);
            pixelsrgb[i] = r << 16 | g << 8 | b;
        }
        return pixelsrgb;
    }
}
