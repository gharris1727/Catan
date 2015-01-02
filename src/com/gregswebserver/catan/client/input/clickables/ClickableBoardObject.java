package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

import java.awt.*;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object referencing a GameObject
 */
public abstract class ClickableBoardObject implements Clickable {

    protected final Coordinate position;
    private final Clickable mapDrag;

    public ClickableBoardObject(Coordinate position, Clickable mapDrag) {
        this.position = position;
        this.mapDrag = mapDrag;
    }

    public void onMouseDrag(Point p) {
        mapDrag.onMouseDrag(p);
    }

    public String toString() {
        return "Position " + position;
    }
}
