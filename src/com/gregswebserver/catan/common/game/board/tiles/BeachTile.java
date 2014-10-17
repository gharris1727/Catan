package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.common.game.board.hexarray.Direction;
import com.gregswebserver.catan.common.util.Statics;

/**
 * Created by Greg on 8/22/2014.
 * A tile that borders the playing field and the ocean.
 */
public class BeachTile extends DirectionalTile {

    private int sides;
    private Graphic graphic;

    public BeachTile(int sides, Direction direction) {
        super(direction);
        this.sides = sides;
        graphic = Statics.nullGraphic;
        switch (sides) {
            case 1:
                switch (direction) {
                    case up:
                        graphic = Statics.beachSingleUp;
                        break;
                    case down:
                        graphic = Statics.beachSingleDown;
                        break;
                    case upleft:
                        graphic = Statics.beachSingleUpLeft;
                        break;
                    case downleft:
                        graphic = Statics.beachSingleDownLeft;
                        break;
                    case upright:
                        graphic = Statics.beachSingleUpRight;
                        break;
                    case downright:
                        graphic = Statics.beachSingleDownRight;
                        break;
                }
                break;
            case 2:
                switch (direction) {
                    case left:
                        graphic = Statics.beachDoubleLeft;
                        break;
                    case right:
                        graphic = Statics.beachDoubleRight;
                        break;
                    case upleft:
                        graphic = Statics.beachDoubleUpLeft;
                        break;
                    case downleft:
                        graphic = Statics.beachDoubleDownLeft;
                        break;
                    case upright:
                        graphic = Statics.beachDoubleUpRight;
                        break;
                    case downright:
                        graphic = Statics.beachDoubleDownRight;
                        break;
                }
                break;
            default:
                graphic = Statics.desertTexture;
                break;
        }
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public String toString() {
        return "BeachTile n/" + sides + " d/" + getDirection();
    }
}
