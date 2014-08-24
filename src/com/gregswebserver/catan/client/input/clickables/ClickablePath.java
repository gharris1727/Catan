package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.paths.Path;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object referencing a path coordinate on the board.
 */
public class ClickablePath extends ClickableBoardObject {

    private Path path;

    public ClickablePath(Coordinate position, Path path) {
        super(position);
        this.path = path;
    }

    public void onRightClick() {

    }

    public void onLeftClick() {

    }

    public String toString() {
        return super.toString() + " Path: " + path;
    }
}
