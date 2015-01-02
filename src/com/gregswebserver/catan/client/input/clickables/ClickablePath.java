package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object referencing a path coordinate on the board.
 */
public class ClickablePath extends ClickableBoardObject {

    private final Path path;

    public ClickablePath(Coordinate position, Clickable mapDrag, Path path) {
        super(position, mapDrag);
        this.path = path;
    }

    public String toString() {
        return super.toString() + " Path: " + path;
    }
}
