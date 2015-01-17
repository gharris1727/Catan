package com.gregswebserver.catan.client.graphics.ui;

import java.awt.*;

/**
 * Created by Greg on 1/17/2015.
 * A style of text, including font, size, italics/bold, and color.
 */
public class TextStyle {

    private final Font font;
    private final Color color;

    public TextStyle(String fontName, int fontStyle, int size, Color color) {
        this.font = new Font(fontName, fontStyle, size);
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public Color getColor() {
        return color;
    }
}
