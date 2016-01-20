package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.graphics.masks.HexagonalMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.common.game.board.BoardObject;

/**
 * Created by Greg on 8/8/2014.
 * Generic hex tile on the Catan GameBoard.
 */
public abstract class Tile extends BoardObject {

    protected static final RenderMask tileMask = new HexagonalMask(Main.staticConfig.getDimension("catan.graphics.tiles.size"));
}
