package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.util.Statics;

/**
 * Created by Greg on 8/22/2014.
 * A tile that fills space in the ocean.
 */
public class OceanTile extends Tile {

    public Graphic getGraphic() {
        return Statics.oceanTexture;
    }

    public String toString() {
        return "OceanTile";
    }
}
