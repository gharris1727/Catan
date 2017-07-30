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
        setSize(size.width, size.height);
        Arrays.fill(widths, width - centerOffset);
        for (int i = 0; i <= (height / 2); i++) {
            padding[i] = (centerOffset * i * 2) / height;
            padding[height - i - 1] = padding[i];
        }
        init();
    }
}
