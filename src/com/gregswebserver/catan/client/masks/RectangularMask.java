package com.gregswebserver.catan.client.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * Generic mask for use with any normal rectangular/square sprite.
 */
public class RectangularMask extends RenderMask {

    private final int width;
    private final int height;

    public RectangularMask(Dimension size) {
        this.width = size.width;
        this.height = size.height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftPadding(int lineNumber) {
        return 0;
    }

    public int getLineWidth(int lineNumber) {
        return width;
    }

    public int getIndex(int x, int y) {
        //More efficient than using the built-in method.
        return y * width + x;
    }


}
