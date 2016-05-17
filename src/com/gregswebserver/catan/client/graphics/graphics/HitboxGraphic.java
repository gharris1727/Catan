package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by greg on 5/16/16.
 * A Graphic that only uses single colors for hitbox calculations.
 */
public class HitboxGraphic extends Graphic {

    public HitboxGraphic(RenderMask mask) {
        super(mask, false);
        loadRaster();
    }

    //Get the color of the secondary array at a specified point.
    public int getClickableColor(Point p) {
        return pixels[mask.getIndex(p)];
    }

    //Copy pixels and clickable pixels, using faster method if possible.
    @Override
    protected void pixelCopy(int[] src, int srcPos, int srcStep, int[] dst, int dstPos, int dstStep, int length, int color, boolean trans) {
        if (dstStep == 1) {
            Arrays.fill(dst, dstPos, dstPos + length, color);
        } else {
            for (int i = 0; i < length; i++) {
                int dstCurr = i * dstStep + dstPos;
                dst[dstCurr] = color;
            }
        }
    }
}
