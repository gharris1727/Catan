package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.util.Direction;

import static com.gregswebserver.catan.client.resources.GraphicSet.BeachDouble;
import static com.gregswebserver.catan.client.resources.GraphicSet.BeachSingle;

/**
 * Created by Greg on 8/22/2014.
 * A tile that borders the playing field and the ocean.
 */
public class BeachTile extends DirectionalTile {

    private static final GraphicSet[] graphics = new GraphicSet[]{
            BeachSingle, BeachDouble
    };

    private int sides;

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
        return graphics[sides - 1].getGraphic(getDirection().ordinal());
    }
}
