package com.gregswebserver.catan.client.masks;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Greg on 8/14/2014.
 * Generic mask for use with any normal rectangular/square sprite.
 */
public class RectangularMask extends RenderMask {

    public RectangularMask(Dimension size) {
        width = size.width;
        height = size.height;
        padding = new int[height];
        widths = new int[height];
        Arrays.fill(widths, width);
        init();
    }

}
