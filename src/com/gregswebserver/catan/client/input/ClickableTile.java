package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/21/2014.
 * represents a clickable tile on the game board.
 */
public class ClickableTile extends ClickableBoardObject {

    public ClickableTile(Coordinate position) {
        super(position);
    }

    @Override
    public GenericEvent onRightClick() {
        return null;
    }

    @Override
    public GenericEvent onLeftClick() {
        return null;
    }
}
