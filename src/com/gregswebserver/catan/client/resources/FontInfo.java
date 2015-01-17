package com.gregswebserver.catan.client.resources;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * A set of cached fonts that can be loaded by the ResourceLoader.
 */
public enum FontInfo {

    Lucida_Console("Lucida Console", Font.PLAIN, 16);

    private final String name;
    private final int style;
    private final int size;


    FontInfo(String name, int style, int size) {
        this.name = name;
        this.style = style;
        this.size = size;
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
}
