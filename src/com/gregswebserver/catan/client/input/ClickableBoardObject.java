package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object referencing a GameObject
 */
public abstract class ClickableBoardObject implements Clickable {

    protected Coordinate position;

    public ClickableBoardObject(Coordinate position) {
        this.position = position;
    }

}
