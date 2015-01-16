package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.util.Direction;

import static com.gregswebserver.catan.client.resources.GraphicInfo.*;

/**
 * Created by Greg on 8/22/2014.
 * A tile that borders the playing field and the ocean.
 */
public class BeachTile extends DirectionalTile {

    private static final GraphicInfo[][] graphics = new GraphicInfo[][]{
            {null, BeachSingleUp, BeachSingleDown, null, null, BeachSingleUpLeft, BeachSingleDownLeft, BeachSingleUpRight, BeachSingleDownRight},
            {null, null, null, BeachDoubleLeft, BeachDoubleRight, BeachDoubleUpLeft, BeachDoubleDownLeft, BeachDoubleUpRight, BeachDoubleDownRight}
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

    public Graphic getGraphic() {
        return ResourceLoader.getGraphic(graphics[sides - 1][getDirection().ordinal()]);
    }
}
