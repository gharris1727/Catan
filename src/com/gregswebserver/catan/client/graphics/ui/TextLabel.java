package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;

import java.awt.*;

/**
 * Created by greg on 1/12/16.
 * A graphical text label
 */
public class TextLabel extends GraphicObject implements Styled {

    private final String textStyleName;

    private UIStyle style;
    private String text;

    public TextLabel(int priority, String textStyleName, String text) {
        super(priority);
        this.textStyleName = textStyleName;
        setText(text);
    }

    @Override
    public void setStyle(UIStyle style) {
        this.style = style;
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        if (style != null && text != null && text.length() > 0)
            setGraphic(new TextGraphic(style, textStyleName, text));
        else
            setGraphic(new Graphic(new RectangularMask(new Dimension(1,1)),true));
    }

    @Override
    public UIStyle getStyle() {
        return style;
    }

    public String toString() {
        return "TextLabel(" + text + ")";
    }
}
