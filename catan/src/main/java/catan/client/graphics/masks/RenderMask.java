package catan.client.graphics.masks;

/**
 * Created by greg on 7/30/17.
 * A type of object that defines a rasterized area made up of contguous segments.
 */
public interface RenderMask {

    int getWidth();

    int getHeight();

    int getLinePadding(int line);

    int getLineWidth(int line);

    default int getIndex(int x, int y) {
        if (!containsPoint(x, y))
            throw new IllegalArgumentException("Coordinate outside of mask bounds.");
        return y * getWidth() + x;
    }

    default boolean hasContent() {
        return getWidth() != 0 && getHeight() != 0;
    }

    default boolean containsPoint(int x, int y) {
        if ((y < 0) || (y >= getHeight()))
            return false;
        int minX = getLinePadding(y);
        int maxX = minX + getLineWidth(y);
        return (x >= minX) && (x < maxX);
    }

    default boolean isAccelerable() {
        return false;
    }
}
