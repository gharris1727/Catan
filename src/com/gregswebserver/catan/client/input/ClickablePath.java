package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object referencing a path coordinate on the board.
 */
public class ClickablePath extends ClickableBoardObject {

    public ClickablePath(Coordinate position) {
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
