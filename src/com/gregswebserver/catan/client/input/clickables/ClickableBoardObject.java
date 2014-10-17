package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object referencing a GameObject
 */
public abstract class ClickableBoardObject implements Clickable {

    protected Coordinate position;

    public ClickableBoardObject(Coordinate position) {
        this.position = position;
    }

    public String toString() {
        return "Position " + position;
    }

    public void onRightClick() {
        //Do nothing.
    }

    public void onLeftClick() {
        //TODO: context menu.
    }

    public void onMiddleClick() {
        //Do nothing.
    }
}
