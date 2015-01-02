package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.client.graphics.areas.MapArea;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * A clickable map object used to manipulate the map generally.
 */
public class ClickableMap implements Clickable {

    private MapArea mapArea;

    public void setMapArea(MapArea mapArea) {
        this.mapArea = mapArea;
    }

    public void onMouseDrag(Point p) {
        mapArea.scroll(p);
    }
}
