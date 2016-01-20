package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.Styled;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;

/**
 * Created by greg on 1/12/16.
 * A graphical text label
 */
public abstract class TextLabel extends GraphicObject implements Styled {

    private final String font;
    private final String text;
    private UIStyle style;

    protected TextLabel(int priority, String font, String text) {
        super(priority);
        this.font = font;
        this.text = text;
    }

    @Override
    public void setStyle(UIStyle style) {
        this.style = style;
        setGraphic(new TextGraphic(style.getFont(font), text));
    }

    @Override
    public UIStyle getStyle() {
        return style;
    }


}
