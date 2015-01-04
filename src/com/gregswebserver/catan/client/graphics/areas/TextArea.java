package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.TextGraphic;
import com.gregswebserver.catan.client.input.TextClickable;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * A GridScreenArea that contains text.
 */
public class TextArea extends GridScreenArea {

    public TextArea(Point position, int priority, TextClickable clickable, Font f, String text) {
        super(position, priority);
        this.clickable = clickable;
        char[] chars = text.toCharArray();
        int[] widths = new int[chars.length];
        int[] heights = new int[1];
        for (int i = 0; i < chars.length; i++) {
            Graphic g = new TextGraphic(f, chars[i]);
            widths[i] = g.getMask().getWidth();
            int height = g.getMask().getHeight();
            if (height > heights[0])
                heights[0] = height;
            add(new StaticGraphic(new Point(i, 0), 0, g, clickable));
            //TODO: figure out how to tell what char was selected.
        }
        super.resize(widths, heights);
    }

    public void resize(Dimension d) {
        //Do nothing, this is non-resizable
    }

    protected void resize(int[] widths, int[] heights) {
        //Do nothing, this is non-resizable
    }
}
