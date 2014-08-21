package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object with a reference to the coordinate for a space for a building.
 */
public class ClickableBuilding extends ClickableBoardObject {

    public ClickableBuilding(Coordinate position) {
        super(position);
    }

    public GenericEvent onRightClick() {
        return null;
    }

    public GenericEvent onLeftClick() {
        return null;
    }
}
