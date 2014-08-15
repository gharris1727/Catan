package com.gregswebserver.catan.graphics;

import java.util.Iterator;

/**
 * Created by Greg on 8/14/2014.
 * Mask for rendering complex shapes to the screen.
 * Returns two Iterators describing the shape that should be rendered.
 */
public abstract class RenderMask {

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getLeftPadding(int lineNumber);

    public abstract int getLineWidth(int lineNumber);

    public Iterator<Integer> getLeftPadding() {
        return new Iterator<Integer>() {
            int cursor = 0;

            public boolean hasNext() {
                return cursor != getHeight();
            }

            public Integer next() {
                int i = cursor;
                if (i >= getHeight())
                    return null;
                cursor++;
                return getLeftPadding(i);
            }
        };
    }

    public Iterator<Integer> getLineWidth() {
        return new Iterator<Integer>() {
            int cursor = 0;

            public boolean hasNext() {
                return cursor != getHeight();
            }

            public Integer next() {
                int i = cursor;
                if (i >= getHeight())
                    return null;
                cursor++;
                return getLineWidth(i);
            }
        };
    }
}
