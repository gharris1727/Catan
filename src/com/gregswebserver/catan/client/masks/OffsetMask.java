package com.gregswebserver.catan.client.masks;

import java.awt.*;

/**
 * Created by Greg on 8/17/2014.
 * A mask that has the same profile as another mask, but everything is offset from the corner.
 */
public class OffsetMask extends RenderMask {

    private final RenderMask other;
    private final Point offset;

    public OffsetMask(RenderMask other, Point offset) {
        this.other = other;
        this.offset = offset;
    }

    public int getWidth() {
        return other.getWidth() + offset.x;
    }

    public int getHeight() {
        return other.getHeight() + offset.y;
    }

    public int getLeftPadding(int lineNumber) {
        return other.getLeftPadding(lineNumber) + offset.x;
    }

    public int getLineWidth(int lineNumber) {
        return other.getLineWidth(lineNumber);
    }
}
