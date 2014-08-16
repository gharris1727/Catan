package com.gregswebserver.catan.client.graphics;

/**
 * Created by Greg on 8/14/2014.
 * Generic mask for use with any normal rectangular/square sprite.
 */
public class RectangularMask extends RenderMask {

    int width;
    int height;

    public RectangularMask(int width, int height) {
        this.width = width;
        this.height = height;
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
