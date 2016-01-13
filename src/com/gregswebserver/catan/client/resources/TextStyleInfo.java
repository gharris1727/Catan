package com.gregswebserver.catan.client.resources;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * A set of cached fonts that can be loaded by the ResourceLoader.
 */
public enum TextStyleInfo {

    UIBlueLight("Lucida Console", Font.PLAIN, 16, Color.WHITE),
    UIBlueDark("Segoe UI", Font.PLAIN, 16, Color.BLACK),
    UIBlueSmall("Segoe UI", Font.PLAIN, 12, Color.BLACK);

    private final String name;
    private final int style;
    private final int size;
    private Color color;

    TextStyleInfo(String name, int style, int size, Color color) {
        this.name = name;
        this.style = style;
        this.size = size;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getStyle() {
        return style;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }
}
