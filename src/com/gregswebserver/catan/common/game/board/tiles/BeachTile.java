package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.common.util.Direction;

/**
 * Created by Greg on 8/22/2014.
 * A tile that borders the playing field and the ocean.
 */
public class BeachTile extends DirectionalTile {

    private final int sides;

    public BeachTile(Direction direction, int sides) {
        super(direction);
        this.sides = sides;
    }

    public int getSides() {
        return sides;
    }

    public String toString() {
        return "BeachTile n/" + sides + " d/" + getDirection();
    }
}
