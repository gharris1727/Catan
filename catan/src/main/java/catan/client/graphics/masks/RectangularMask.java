package catan.client.graphics.masks;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Greg on 8/14/2014.
 * Generic mask for use with any normal rectangular/square sprite.
 */
public class RectangularMask extends RenderMask {

    public RectangularMask(Dimension size) {
        setSize(size.width, size.height);
        Arrays.fill(widths, width);
        init();
    }

}
