package com.gregswebserver.catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/21/2014.
 * A rectangular mask except the corners are rounded off
 */
public class RoundedRectangularMask extends RenderMask {

    private static final int scale = 4;

    private RenderMask corner;

    public RoundedRectangularMask(Dimension square) {
        //Auto-sizes the corners to be proportional to the whole window.
        int radius = (square.width > square.height) ? square.height / scale : square.width / scale;
        init(square, new Dimension(radius, radius));
    }

    public RoundedRectangularMask(Dimension square, Dimension corner) {
        //Uses the external dimensions given for customization.
        init(square, corner);
    }

    private void init(Dimension square, Dimension corner) {
        this.width = square.width;
        this.height = square.height;
        this.corner = new RoundMask(corner);
        padding = new int[height];
        widths = new int[height];
        for (int i = 1; i < height; i++) {
            padding[i] = getPadding(i);
            widths[i] = getWidth(i);
        }
        init();
    }

    private int getPadding(int lineNumber) {
        if (lineNumber < corner.getWidth() / 2)
            return corner.padding[lineNumber];
        if (getHeight() - 1 - lineNumber < corner.getHeight() / 2)
            return getPadding(getHeight() - 1 - lineNumber);
        return 0;
    }

    private int getWidth(int lineNumber) {
        return width - (2 * getPadding(lineNumber));
    }
}
