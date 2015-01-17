package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.client.resources.TextStyleInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import static com.gregswebserver.catan.client.resources.GraphicSet.*;
import static com.gregswebserver.catan.client.resources.TextStyleInfo.UIBlueDark;
import static com.gregswebserver.catan.client.resources.TextStyleInfo.UIBlueLight;

/**
 * Created by Greg on 1/15/2015.
 * A background used to render a ui background, that is tiled to fit multiple sizes.
 */
public enum UIStyle {

    Blue(UIBlueBackground, UIBlueWindow, UIBlueText, UIBlueButton, UIBlueLight, UIBlueDark);

    private final GraphicSet background;
    private final GraphicSet windows;
    private final GraphicSet text;
    private final GraphicSet buttons;
    //TODO: redefine the font loading so that it is more flexible.
    private final TextStyleInfo light;
    private final TextStyleInfo dark;

    UIStyle(GraphicSet background, GraphicSet windows, GraphicSet text, GraphicSet buttons, TextStyleInfo light, TextStyleInfo dark) {
        //TODO: abstract the background styles further, not good to expose the GraphicSet.
        this.background = background;
        this.windows = windows;
        this.text = text;
        this.buttons = buttons;
        this.light = light;
        this.dark = dark;
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

    public TextStyle getLightTextStyle() {
        return ResourceLoader.getTextStyle(light);
    }

    public TextStyle getDarkTextStyle() {
        return ResourceLoader.getTextStyle(dark);
    }
}
