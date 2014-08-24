package com.gregswebserver.catan.client.masks;

import java.awt.*;

/**
 * Created by Greg on 8/21/2014.
 * A rectangular mask except the corners are rounded off
 */
public class RoundedRectangularMask extends RenderMask {

    private int width, height;
    private int radius;
    private RoundedMask roundMask;

    public RoundedRectangularMask(Dimension square) {
        //Auto-sizes the corners to be proportional to the whole window.
        int radius = (square.width > square.height) ? square.height / 8 : square.width / 8;
        this.width = square.width;
        this.height = square.height;
        roundMask = new RoundedMask(new Dimension(radius, radius));
    }

    public RoundedRectangularMask(Dimension square, Dimension round) {
        this.width = square.width;
        this.height = square.height;
        roundMask = new RoundedMask(round);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftPadding(int lineNumber) {
        if (lineNumber < radius)
            return roundMask.getLeftPadding(lineNumber);
        if (getHeight() - 1 - lineNumber < radius)
            return getLeftPadding(getHeight() - 1 - lineNumber);
        return 0;
    }

    public int getLineWidth(int lineNumber) {
        return width - (2 * getLeftPadding(lineNumber));
    }
}
