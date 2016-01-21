package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.HexagonalMask;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.util.Direction;

/**
 * Created by Greg on 8/22/2014.
 * A tile that borders the playing field and the ocean.
 */
public class BeachTile extends DirectionalTile {

    private static final GraphicSet singleBeach;
    private static final GraphicSet doubleBeach;

    static {
        singleBeach = new GraphicSet("catan.graphics.tiles.singlebeach", HexagonalMask.class);
        doubleBeach = new GraphicSet("catan.graphics.tiles.doublebeach", HexagonalMask.class);
    }

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

    @Override
    public Graphic getGraphic() {
        switch (sides) {
            case 1:
                return singleBeach.getGraphic(getDirection().ordinal());
            case 2:
                return doubleBeach.getGraphic(getDirection().ordinal());
        }
        throw new NotYetRenderableException("Invalid beach tile configuration");
    }
}
