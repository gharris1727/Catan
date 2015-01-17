package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.util.TextGraphic;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 1/16/2015.
 * A static object that contains text.
 */
public abstract class TextObject extends StaticObject {

    public TextObject(Point position, int priority, Font font, String text, Clickable redirect) {
        super(position, priority, new TextGraphic(font, text), redirect);
    }

    protected TextObject(Point position, int priority, Font font, String text) {
        super(position, priority, new TextGraphic(font, text));
    }
}
