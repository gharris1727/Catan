package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.TextGraphic;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * A GridScreenArea that contains text.
 */
public abstract class TextArea extends GridScreenArea {

    public TextArea(Point position, int priority, Font f, String text) {
        super(position, priority);
        char[] chars = text.toCharArray();
        int[] widths = new int[chars.length];
        int[] heights = new int[1];
        for (int i = 0; i < chars.length; i++) {
            Graphic g = new TextGraphic(f, chars[i]);
            widths[i] = g.getMask().getWidth();
            int height = g.getMask().getHeight();
            if (height > heights[0])
                heights[0] = height;
            add(charObject(i, 0, g));
        }
        super.resize(widths, heights);
    }

    protected abstract StaticGraphic charObject(int index, int priority, Graphic g);

    public void resize(Dimension d) {
        //Do nothing, this is non-resizable
    }

    protected void resize(int[] widths, int[] heights) {
        //Do nothing, this is non-resizable
    }
}
