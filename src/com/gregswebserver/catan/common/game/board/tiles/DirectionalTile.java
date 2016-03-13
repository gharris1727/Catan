package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.common.util.Direction;

/**
 * Created by Greg on 8/22/2014.
 * A tile that has directionality to it, and can report the edges/vertices that it is pointing at.
 */
public abstract class DirectionalTile extends Tile {

    private final Direction direction;

    protected DirectionalTile(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectionalTile)) return false;

        DirectionalTile that = (DirectionalTile) o;

        return direction == that.direction;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }
}
