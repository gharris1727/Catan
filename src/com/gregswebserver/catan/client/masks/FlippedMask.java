package com.gregswebserver.catan.client.masks;

/**
 * Created by Greg on 8/19/2014.
 * Takes another render mask and becomes the mirrored version of it.
 */
public class FlippedMask extends RenderMask {

    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    private RenderMask other;
    private int direction;

    public FlippedMask(RenderMask other, int direction) {
        this.other = other;
        this.direction = direction;
    }

    public int getWidth() {
        return other.getWidth();
    }

    public int getHeight() {
        return other.getHeight();
    }

    public int getLeftPadding(int lineNumber) {
        switch (direction) {
            case VERTICAL:
                return other.getLeftPadding(getHeight() - 1 - lineNumber);
            case HORIZONTAL:
            default:
                return getWidth() - getLineWidth(lineNumber) - other.getLeftPadding(lineNumber);
        }
    }

    public int getLineWidth(int lineNumber) {
        switch (direction) {
            case VERTICAL:
                return other.getLineWidth(getHeight() - 1 - lineNumber);
            case HORIZONTAL:
            default:
                return other.getLineWidth(lineNumber);
        }
    }
}
