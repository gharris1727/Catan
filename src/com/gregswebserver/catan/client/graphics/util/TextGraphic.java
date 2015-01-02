package com.gregswebserver.catan.client.graphics.util;

/**
 * Created by Greg on 8/24/2014.
 * graphical object that takes a text string and displays it.
 */
public class TextGraphic extends Graphic {

    private String text;
    private GraphicFont font;

    public TextGraphic(String text, GraphicFont font) {
        super();
        this.text = text;
        this.font = font;
    }

    public String toString() {
        return super.toString() + "Text: " + text;
    }
}
