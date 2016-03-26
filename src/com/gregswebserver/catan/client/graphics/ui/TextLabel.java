package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;

import java.awt.*;

/**
 * Created by greg on 1/12/16.
 * A graphical text label
 */
public class TextLabel extends ConfigurableGraphicObject {

    private String text;

    public TextLabel(int priority, String configKey, String text) {
        super(priority, configKey);
        setText(text);
    }

    @Override
    public void loadConfig(UIConfig config) {
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        if (isRenderable() && text != null && text.length() > 0)
            setGraphic(new TextGraphic(getConfig(), getConfig().getLayout().get("style"), text));
        else
            setGraphic(new Graphic(new RectangularMask(new Dimension(1, 1)), true));
    }

    public String toString() {
        return "TextLabel(" + text + ")";
    }
}
