package catan.client.graphics.masks;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by greg on 3/13/16.
 * Mask in the shape of a Chevron
 */
public class ChevronMask extends RenderMask {

    public ChevronMask(Dimension size) {
        this(size, size.width/4);
    }

    public ChevronMask(Dimension size, int centerOffset) {
        width = size.width;
        height = size.height;
        padding = new int[height];
        widths = new int[height];
        Arrays.fill(widths, width - centerOffset);
        for (int i = 0; i <= height/2; i++)
            padding[i] = padding[height-i-1] = (centerOffset * i * 2) / height;
        init();
    }
}
