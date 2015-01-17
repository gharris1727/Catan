package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.resources.FontInfo;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.awt.*;

import static com.gregswebserver.catan.client.resources.FontInfo.Lucida_Console;
import static com.gregswebserver.catan.client.resources.GraphicSet.*;

/**
 * Created by Greg on 1/15/2015.
 * A background used to render a ui background, that is tiled to fit multiple sizes.
 */
public enum UIStyle {

    Blue(UIBlueBackground, UIBlueWindow, UIBlueText, UIBlueButton, Lucida_Console);

    private final GraphicSet background;
    private final GraphicSet windows;
    private final GraphicSet text;
    private final GraphicSet buttons;
    private final FontInfo font;

    UIStyle(GraphicSet background, GraphicSet windows, GraphicSet text, GraphicSet buttons, FontInfo font) {
        //TODO: abstract the background styles further, not good to expose the GraphicSet.
        this.background = background;
        this.windows = windows;
        this.text = text;
        this.buttons = buttons;
        this.font = font;
    }

    public GraphicSet getBackgroundStyle() {
        return background;
    }

    public GraphicSet getInterfaceStyle() {
        return windows;
    }

    public GraphicSet getTextStyle() {
        return text;
    }

    public GraphicSet getButtonStyle() {
        return buttons;
    }

    public Font getFont() {
        return ResourceLoader.getFont(font);
    }
}
