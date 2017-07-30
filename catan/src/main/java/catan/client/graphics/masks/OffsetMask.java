package catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/17/2014.
 * A mask that has the same profile as another mask, but everything is offset from the corner.
 */
public class OffsetMask extends RenderMask {

    public OffsetMask(RenderMask other, Point offset) {
        setSize(other.width + offset.x, other.height + offset.y);
        for (int i = 0; i < height; i++) {
            int pad = other.padding[i];
            int wid = other.widths[i];
            if (pad < offset.x) {
                padding[i] = 0;
                widths[i] = (wid + pad) - offset.x;
            } else {
                padding[i] = pad - offset.x;
                widths[i] = wid;
            }
        }
        init();
    }
}
