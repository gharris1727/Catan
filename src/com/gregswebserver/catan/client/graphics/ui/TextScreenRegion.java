package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.screen.GridScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.util.CharGraphic;
import com.gregswebserver.catan.client.graphics.util.Graphic;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * A GridScreenArea that contains text.
 */
public abstract class TextScreenRegion extends GridScreenRegion {

    protected Graphic[] chars;
    private Font font;

    public TextScreenRegion(Point position, int priority, Font font, String text) {
        this(position, priority, font);
        setText(text);
    }

    protected TextScreenRegion(Point position, int priority, Font font) {
        super(position, priority);
        this.font = font;
    }

    public void setText(String text) {
        char[] arr = text.toCharArray();
        chars = new Graphic[arr.length];
        int[] widths = new int[arr.length];
        int[] heights = new int[1];
        for (int i = 0; i < arr.length; i++) {
            chars[i] = new CharGraphic(font, arr[i]);
            widths[i] = chars[i].getMask().getWidth();
            int height = chars[i].getMask().getHeight();
            if (height > heights[0])
                heights[0] = height;
        }
        super.setGridSize(widths, heights);
    }

    protected void render() {
        clear();
        for (int i = 0; i < chars.length; i++)
            add(new CharStaticObject(i, i, chars[i]));
    }

    public void setSize(Dimension d) {
        //Do nothing, this is non-resizable
    }

    private class CharStaticObject extends StaticObject {

        public CharStaticObject(int position, int priority, Graphic graphic) {
            super(new Point(position, 0), priority, graphic);
            setClickable(TextScreenRegion.this);
        }

        public String toString() {
            return "CharStaticObject #" + getPosition().x + " inside " + TextScreenRegion.this;
        }
    }
}
