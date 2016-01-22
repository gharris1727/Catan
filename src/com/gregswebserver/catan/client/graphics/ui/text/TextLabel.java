package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.Styled;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;

import java.awt.*;

/**
 * Created by greg on 1/12/16.
 * A graphical text label
 */
public abstract class TextLabel extends GraphicObject implements Styled {

    private final String font;
    private String text;
    private UIStyle style;

    protected TextLabel(int priority, String font, String text) {
        super(priority);
        this.font = font;
        setText(text);
    }

    @Override
    public void setStyle(UIStyle style) {
        this.style = style;
        if (style != null && text != null && text.length() > 0)
            setGraphic(new TextGraphic(style.getFont(font), text));
        else
            setGraphic(new Graphic(new RectangularMask(new Dimension(0,0))));
    }

    public void setText(String text) {
        this.text = text;
        if (style != null && text != null && text.length() > 0)
            setGraphic(new TextGraphic(style.getFont(font), text));
        else
            setGraphic(new Graphic(new RectangularMask(new Dimension(0,0))));
    }

    @Override
    public UIStyle getStyle() {
        return style;
    }


}
